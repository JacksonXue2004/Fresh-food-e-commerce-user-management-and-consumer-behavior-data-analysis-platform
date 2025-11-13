package com.ruoyi.freshdata.service.impl;

import com.ruoyi.freshdata.domain.AnalysisResult;
import com.ruoyi.freshdata.domain.UserBehaviorData;
import com.ruoyi.freshdata.service.IDataAnalysisService;
import com.ruoyi.freshdata.util.CsvReaderUtil;
import com.ruoyi.freshdata.util.CityNameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据分析服务实现类
 * 
 * @author ruoyi
 */
@Service
public class DataAnalysisServiceImpl implements IDataAnalysisService {
    
    private static final Logger log = LoggerFactory.getLogger(DataAnalysisServiceImpl.class);
    
    /** CSV文件名 */
    private static final String CSV_FILE_NAME = "app_data_cleaned.csv";
    
    /** 数据缓存 */
    private List<UserBehaviorData> userDataCache;
    
    /** 城市拼音到中文的映射 */
    private static final Map<String, String> CITY_NAME_MAP = new HashMap<String, String>() {{
        put("beijing", "北京");
        put("shanghai", "上海");
        put("guangzhou", "广州");
        put("shenzhen", "深圳");
        put("chengdu", "成都");
        put("hangzhou", "杭州");
        put("chongqing", "重庆");
        put("wuhan", "武汉");
        put("xian", "西安");
        put("suzhou", "苏州");
        put("tianjin", "天津");
        put("nanjing", "南京");
        put("changsha", "长沙");
        put("zhengzhou", "郑州");
        put("dongguan", "东莞");
        put("qingdao", "青岛");
        put("shenyang", "沈阳");
        put("ningbo", "宁波");
        put("kunming", "昆明");
        put("jinan", "济南");
        put("dalian", "大连");
        put("xiamen", "厦门");
        put("foshan", "佛山");
        put("hefei", "合肥");
        put("fuzhou", "福州");
        put("harbin", "哈尔滨");
        put("changchun", "长春");
        put("wuxi", "无锡");
        put("shijiazhuang", "石家庄");
        put("taiyuan", "太原");
    }};
    
    /**
     * 初始化时加载数据
     */
    @PostConstruct
    public void init() {
        log.info("开始加载用户行为数据...");
        refreshDataCache();
    }
    
    @Override
    public void refreshDataCache() {
        this.userDataCache = CsvReaderUtil.readCsvData(CSV_FILE_NAME);
        log.info("用户行为数据加载完成，共{}条记录", userDataCache.size());
    }
    
    @Override
    public List<UserBehaviorData> getAllUserData() {
        return new ArrayList<>(userDataCache);
    }
    
    @Override
    public AnalysisResult getDataOverview() {
        AnalysisResult result = new AnalysisResult();
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 总用户数
            long totalUsers = userDataCache.size();
            data.put("totalUsers", totalUsers);
            log.info("总用户数: {}", totalUsers);
            
            // 平均首单金额
            double avgFirstPrice = userDataCache.stream()
                    .filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 0)
                    .mapToDouble(UserBehaviorData::getFirstOrderPrice)
                    .average()
                    .orElse(0.0);
            data.put("avgFirstPrice", String.format("%.2f", avgFirstPrice));
            log.info("平均首单金额: {}", avgFirstPrice);
            
            // 平均账户年龄（月）
            double avgAge = userDataCache.stream()
                    .filter(d -> d.getAgeMonth() != null && d.getAgeMonth() > 0)
                    .mapToDouble(UserBehaviorData::getAgeMonth)
                    .average()
                    .orElse(0.0);
            data.put("avgAge", String.format("%.1f", avgAge));
            
            // 平均登录天数
            double avgLoginDays = userDataCache.stream()
                    .filter(d -> d.getLoginDay() != null && d.getLoginDay() > 0)
                    .mapToDouble(UserBehaviorData::getLoginDay)
                    .average()
                    .orElse(0.0);
            data.put("avgLoginDays", String.format("%.1f", avgLoginDays));
            
            // 首单金额分布
            List<String> priceRanges = Arrays.asList("0-50元", "50-100元", "100-200元", "200-500元", "500+元");
            List<Long> priceCounts = Arrays.asList(
                userDataCache.stream().filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 0 && d.getFirstOrderPrice() <= 50).count(),
                userDataCache.stream().filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 50 && d.getFirstOrderPrice() <= 100).count(),
                userDataCache.stream().filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 100 && d.getFirstOrderPrice() <= 200).count(),
                userDataCache.stream().filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 200 && d.getFirstOrderPrice() <= 500).count(),
                userDataCache.stream().filter(d -> d.getFirstOrderPrice() != null && d.getFirstOrderPrice() > 500).count()
            );
            data.put("priceRanges", priceRanges);
            data.put("priceCounts", priceCounts);
            
            // 年龄分布（按月龄分组）
            List<Map<String, Object>> ageGroups = new ArrayList<>();
            ageGroups.add(createGroup("0-3个月", userDataCache.stream().filter(d -> d.getAgeMonth() != null && d.getAgeMonth() >= 0 && d.getAgeMonth() <= 3).count()));
            ageGroups.add(createGroup("3-6个月", userDataCache.stream().filter(d -> d.getAgeMonth() != null && d.getAgeMonth() > 3 && d.getAgeMonth() <= 6).count()));
            ageGroups.add(createGroup("6-12个月", userDataCache.stream().filter(d -> d.getAgeMonth() != null && d.getAgeMonth() > 6 && d.getAgeMonth() <= 12).count()));
            ageGroups.add(createGroup("12个月以上", userDataCache.stream().filter(d -> d.getAgeMonth() != null && d.getAgeMonth() > 12).count()));
            data.put("ageGroups", ageGroups);
            
            // 城市分布TOP10
            long totalWithCity = userDataCache.stream().filter(d -> d.getCityNum() != null).count();
            long nonEmptyCity = userDataCache.stream().filter(d -> d.getCityNum() != null && !d.getCityNum().trim().isEmpty()).count();
            log.info("总记录数: {}, 有cityNum的: {}, 非空cityNum的: {}", userDataCache.size(), totalWithCity, nonEmptyCity);
            
            Map<String, Long> cityCount = userDataCache.stream()
                    .filter(d -> d.getCityNum() != null && !d.getCityNum().trim().isEmpty())
                    .collect(Collectors.groupingBy(UserBehaviorData::getCityNum, Collectors.counting()));
            log.info("城市去重后数量: {}", cityCount.size());
            
            List<Map<String, Object>> topCities = cityCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(e -> {
                        Map<String, Object> city = new HashMap<>();
                        city.put("name", getCityChineseName(e.getKey())); // 转换为中文名
                        city.put("value", e.getValue());
                        return city;
                    })
                    .collect(Collectors.toList());
            data.put("topCities", topCities);
            log.info("城市分布TOP10数量: {}", topCities.size());
            
            // 登录行为雷达图数据（归一化到0-100）
            double maxLoginDay = Math.max(1.0, userDataCache.stream().filter(d -> d.getLoginDay() != null).mapToDouble(UserBehaviorData::getLoginDay).max().orElse(1.0));
            double maxLoginTime = Math.max(1.0, userDataCache.stream().filter(d -> d.getLoginTime() != null).mapToDouble(UserBehaviorData::getLoginTime).max().orElse(1.0));
            double maxLaunchTime = Math.max(1.0, userDataCache.stream().filter(d -> d.getLaunchTime() != null).mapToDouble(UserBehaviorData::getLaunchTime).max().orElse(1.0));
            double maxVisitNum = Math.max(1.0, userDataCache.stream().filter(d -> d.getVisitNum() != null).mapToDouble(UserBehaviorData::getVisitNum).max().orElse(1.0));
            double maxSearchNum = Math.max(1.0, userDataCache.stream().filter(d -> d.getSearchNum() != null).mapToDouble(UserBehaviorData::getSearchNum).max().orElse(1.0));
            
            double avgLoginTime = userDataCache.stream().filter(d -> d.getLoginTime() != null).mapToDouble(UserBehaviorData::getLoginTime).average().orElse(0.0);
            double avgLaunchTime = userDataCache.stream().filter(d -> d.getLaunchTime() != null).mapToDouble(UserBehaviorData::getLaunchTime).average().orElse(0.0);
            double avgVisitNum = userDataCache.stream().filter(d -> d.getVisitNum() != null).mapToDouble(UserBehaviorData::getVisitNum).average().orElse(0.0);
            double avgSearchNum = userDataCache.stream().filter(d -> d.getSearchNum() != null).mapToDouble(UserBehaviorData::getSearchNum).average().orElse(0.0);
            
            List<Double> loginRadar = Arrays.asList(
                round((avgLoginDays / maxLoginDay) * 100, 1),
                round((avgLoginTime / maxLoginTime) * 100, 1),
                round((avgLaunchTime / maxLaunchTime) * 100, 1),
                round((avgVisitNum / maxVisitNum) * 100, 1),
                round((avgSearchNum / maxSearchNum) * 100, 1)
            );
            data.put("loginRadar", loginRadar);
            
            // 设备分布
            Map<Double, Long> deviceCount = userDataCache.stream()
                    .filter(d -> d.getModelNum() != null)
                    .collect(Collectors.groupingBy(UserBehaviorData::getModelNum, Collectors.counting()));
            List<Map<String, Object>> deviceGroups = deviceCount.entrySet().stream()
                    .sorted(Map.Entry.<Double, Long>comparingByValue().reversed())
                    .limit(5)
                    .map(e -> {
                        Map<String, Object> device = new HashMap<>();
                        device.put("name", "设备" + String.format("%.0f", e.getKey()));
                        device.put("value", e.getValue());
                        return device;
                    })
                    .collect(Collectors.toList());
            data.put("deviceGroups", deviceGroups);
            
            log.info("数据概览计算完成，data keys: {}", data.keySet());
        } catch (Exception e) {
            log.error("计算数据概览时发生错误", e);
            throw new RuntimeException("数据分析失败", e);
        }
        
        result.setData(data);
        return result;
    }
    
    private Map<String, Object> createGroup(String name, long value) {
        Map<String, Object> group = new HashMap<>();
        group.put("name", name);
        group.put("value", value);
        return group;
    }
    
    /**
     * 将城市拼音转换为中文名
     */
    private String getCityChineseName(String pinyin) {
        return CITY_NAME_MAP.getOrDefault(pinyin, pinyin);
    }
    
    @Override
    public Map<String, Long> getUserActiveDistribution() {
        Map<String, Long> distribution = new LinkedHashMap<>();
        
        long activeCount = userDataCache.stream()
                .filter(data -> data.getResult() != null && data.getResult() == 1)
                .count();
        long inactiveCount = userDataCache.size() - activeCount;
        
        distribution.put("活跃用户", activeCount);
        distribution.put("不活跃用户", inactiveCount);
        
        return distribution;
    }
    
    @Override
    public AnalysisResult getLoginBehaviorAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 登录天数分布数据
        List<String> loginDaysRanges = Arrays.asList("1-3天", "4-7天", "8-15天", "15天以上");
        List<Long> loginDaysCounts = Arrays.asList(
            countByLoginDays(1, 3),
            countByLoginDays(4, 7),
            countByLoginDays(8, 15),
            countByLoginDays(15, Integer.MAX_VALUE)
        );
        result.addData("loginDaysRanges", loginDaysRanges);
        result.addData("loginDaysCounts", loginDaysCounts);
        
        // 登录时长分组
        List<Map<String, Object>> loginTimeGroups = new ArrayList<>();
        loginTimeGroups.add(createGroup("短时长(<5分钟)", countByLoginTime(0, 5)));
        loginTimeGroups.add(createGroup("中等时长(5-15分钟)", countByLoginTime(5, 15)));
        loginTimeGroups.add(createGroup("长时长(15-30分钟)", countByLoginTime(15, 30)));
        loginTimeGroups.add(createGroup("超长时长(>30分钟)", countByLoginTime(30, Double.MAX_VALUE)));
        result.addData("loginTimeGroups", loginTimeGroups);
        
        // 启动次数与登录天数关系数据（散点图）
        List<List<Double>> launchLoginData = userDataCache.stream()
                .filter(d -> d.getLoginDay() != null && d.getLaunchTime() != null)
                .limit(200) // 限制数量避免过多
                .map(d -> Arrays.asList(d.getLoginDay(), d.getLaunchTime()))
                .collect(Collectors.toList());
        result.addData("launchLoginData", launchLoginData);
        
        return result;
    }
    
    @Override
    public AnalysisResult getBrowseBehaviorAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 页面访问雷达图数据 (归一化到0-100)
        double maxMainHome = userDataCache.stream().filter(d -> d.getMainHome() != null).mapToDouble(UserBehaviorData::getMainHome).max().orElse(1.0);
        double maxMainPage = userDataCache.stream().filter(d -> d.getMainPage() != null).mapToDouble(UserBehaviorData::getMainPage).max().orElse(1.0);
        double maxShopCart = userDataCache.stream().filter(d -> d.getShopCartPage() != null).mapToDouble(UserBehaviorData::getShopCartPage).max().orElse(1.0);
        double maxMainMime = userDataCache.stream().filter(d -> d.getMainMime() != null).mapToDouble(UserBehaviorData::getMainMime).max().orElse(1.0);
        double maxCollection = userDataCache.stream().filter(d -> d.getCollectionTab() != null).mapToDouble(UserBehaviorData::getCollectionTab).max().orElse(1.0);
        
        List<Double> pageVisits = Arrays.asList(
            round((userDataCache.stream().filter(d -> d.getMainHome() != null).mapToDouble(UserBehaviorData::getMainHome).average().orElse(0.0) / maxMainHome) * 100, 1),
            round((userDataCache.stream().filter(d -> d.getMainPage() != null).mapToDouble(UserBehaviorData::getMainPage).average().orElse(0.0) / maxMainPage) * 100, 1),
            round((userDataCache.stream().filter(d -> d.getShopCartPage() != null).mapToDouble(UserBehaviorData::getShopCartPage).average().orElse(0.0) / maxShopCart) * 100, 1),
            round((userDataCache.stream().filter(d -> d.getMainMime() != null).mapToDouble(UserBehaviorData::getMainMime).average().orElse(0.0) / maxMainMime) * 100, 1),
            round((userDataCache.stream().filter(d -> d.getCollectionTab() != null).mapToDouble(UserBehaviorData::getCollectionTab).average().orElse(0.0) / maxCollection) * 100, 1)
        );
        result.addData("pageVisits", pageVisits);
        
        // 详情页访问分布
        List<String> detailRanges = Arrays.asList("0-5次", "5-10次", "10-20次", "20次以上");
        List<Long> detailCounts = Arrays.asList(
            countByDetailNum(0, 5),
            countByDetailNum(5, 10),
            countByDetailNum(10, 20),
            countByDetailNum(20, Double.MAX_VALUE)
        );
        result.addData("detailRanges", detailRanges);
        result.addData("detailCounts", detailCounts);
        
        // 浏览漏斗数据
        long totalUsers = userDataCache.size();
        long visitUsers = userDataCache.stream().filter(d -> d.getVisitNum() != null && d.getVisitNum() > 0).count();
        long searchUsers = userDataCache.stream().filter(d -> d.getSearchNum() != null && d.getSearchNum() > 0).count();
        long detailUsers = userDataCache.stream().filter(d -> d.getDetailNum() != null && d.getDetailNum() > 0).count();
        long cartUsers = userDataCache.stream().filter(d -> d.getShopCartPage() != null && d.getShopCartPage() > 0).count();
        
        List<Map<String, Object>> browseFunnel = new ArrayList<>();
        browseFunnel.add(createFunnelData("访问", round((double) visitUsers / totalUsers * 100, 2)));
        browseFunnel.add(createFunnelData("搜索", round((double) searchUsers / totalUsers * 100, 2)));
        browseFunnel.add(createFunnelData("查看详情", round((double) detailUsers / totalUsers * 100, 2)));
        browseFunnel.add(createFunnelData("加入购物车", round((double) cartUsers / totalUsers * 100, 2)));
        result.addData("browseFunnel", browseFunnel);
        
        // 搜索相关数据
        List<String> searchRanges = Arrays.asList("0-5次", "5-10次", "10-20次", "20次以上");
        List<Long> searchCounts = Arrays.asList(
            countBySearchNum(0, 5),
            countBySearchNum(5, 10),
            countBySearchNum(10, 20),
            countBySearchNum(20, Double.MAX_VALUE)
        );
        result.addData("searchRanges", searchRanges);
        result.addData("searchCounts", searchCounts);
        
        // 搜索完成率
        double finishRate = searchUsers > 0 ? round((double) detailUsers / searchUsers * 100, 2) : 0.0;
        result.addData("finishRate", finishRate);
        
        return result;
    }
    
    @Override
    public AnalysisResult getConsumptionAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 1. 平均订单金额
        double avgOrderAmount = userDataCache.stream()
                .filter(data -> data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .mapToDouble(UserBehaviorData::getFirstOrderPrice)
                .average()
                .orElse(0.0);
        result.addData("avgOrderAmount", round(avgOrderAmount, 2));
        
        // 2. 平均购物车商品（用购物车访问页面次数近似）
        double avgCartItems = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0)
                .mapToDouble(UserBehaviorData::getShopCartPage)
                .average()
                .orElse(0.0);
        result.addData("avgCartItems", round(avgCartItems, 2));
        
        // 3. 优惠券使用率
        long totalUsers = userDataCache.size();
        long couponUsers = userDataCache.stream()
                .filter(data -> data.getCouponNum() != null && data.getCouponNum() > 0)
                .count();
        double couponUsageRate = totalUsers > 0 ? round((double) couponUsers / totalUsers * 100, 2) : 0.0;
        result.addData("couponUsageRate", couponUsageRate);
        
        // 4. 购物车转化率（访问购物车且完成首单的比例）
        long cartUsers = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0)
                .count();
        long cartConversionUsers = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0
                        && data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .count();
        double cartConversionRate = cartUsers > 0 ? round((double) cartConversionUsers / cartUsers * 100, 2) : 0.0;
        result.addData("cartConversionRate", cartConversionRate);
        
        // 5. 首单金额分布
        List<String> amountRanges = Arrays.asList("0元", "0-50元", "50-100元", "100-200元", "200元以上");
        List<Long> amountCounts = Arrays.asList(
            userDataCache.stream().filter(data -> data.getFirstOrderPrice() != null && data.getFirstOrderPrice() == 0).count(),
            countByOrderPrice(0.1, 50),
            countByOrderPrice(50, 100),
            countByOrderPrice(100, 200),
            countByOrderPrice(200, Double.MAX_VALUE)
        );
        result.addData("amountRanges", amountRanges);
        result.addData("amountCounts", amountCounts);
        
        // 6. 购物车行为分布
        long cartPageVisits = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null)
                .mapToLong(data -> data.getShopCartPage().longValue())
                .sum();
        long mainPageVisits = userDataCache.stream()
                .filter(data -> data.getMainPage() != null)
                .mapToLong(data -> data.getMainPage().longValue())
                .sum();
        long detailPageVisits = userDataCache.stream()
                .filter(data -> data.getDetailNum() != null)
                .mapToLong(data -> data.getDetailNum().longValue())
                .sum();
        
        List<Map<String, Object>> cartBehavior = new ArrayList<>();
        cartBehavior.add(createGroup("购物车页面访问", cartPageVisits));
        cartBehavior.add(createGroup("商品详情浏览", detailPageVisits));
        cartBehavior.add(createGroup("主页浏览", mainPageVisits));
        result.addData("cartBehavior", cartBehavior);
        
        // 7. 优惠券使用分布
        List<String> couponRanges = Arrays.asList("0张", "1-2张", "3-5张", "5张以上");
        List<Long> couponCounts = Arrays.asList(
            userDataCache.stream().filter(data -> data.getCouponNum() == null || data.getCouponNum() == 0).count(),
            userDataCache.stream().filter(data -> data.getCouponNum() != null && data.getCouponNum() > 0 && data.getCouponNum() <= 2).count(),
            userDataCache.stream().filter(data -> data.getCouponNum() != null && data.getCouponNum() > 2 && data.getCouponNum() <= 5).count(),
            userDataCache.stream().filter(data -> data.getCouponNum() != null && data.getCouponNum() > 5).count()
        );
        result.addData("couponRanges", couponRanges);
        result.addData("couponCounts", couponCounts);
        
        // 8. 购买转化漏斗
        long visitUsers = userDataCache.stream()
                .filter(data -> data.getVisitNum() != null && data.getVisitNum() > 0)
                .count();
        long detailUsers = userDataCache.stream()
                .filter(data -> data.getDetailNum() != null && data.getDetailNum() > 0)
                .count();
        long addCartUsers = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0)
                .count();
        long getCouponUsers = userDataCache.stream()
                .filter(data -> data.getCouponNum() != null && data.getCouponNum() > 0)
                .count();
        long purchaseUsers = userDataCache.stream()
                .filter(data -> data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .count();
        
        List<Map<String, Object>> conversionFunnel = new ArrayList<>();
        Map<String, Object> step1 = new HashMap<>();
        step1.put("value", visitUsers);
        step1.put("name", "访问商品");
        conversionFunnel.add(step1);
        
        Map<String, Object> step2 = new HashMap<>();
        step2.put("value", detailUsers);
        step2.put("name", "查看详情");
        conversionFunnel.add(step2);
        
        Map<String, Object> step3 = new HashMap<>();
        step3.put("value", addCartUsers);
        step3.put("name", "加入购物车");
        conversionFunnel.add(step3);
        
        Map<String, Object> step4 = new HashMap<>();
        step4.put("value", getCouponUsers);
        step4.put("name", "领取优惠券");
        conversionFunnel.add(step4);
        
        Map<String, Object> step5 = new HashMap<>();
        step5.put("value", purchaseUsers);
        step5.put("name", "完成购买");
        conversionFunnel.add(step5);
        
        result.addData("conversionFunnel", conversionFunnel);
        
        // 9. 购买频次分布（用点击购买次数近似）
        List<Map<String, Object>> purchaseFrequency = new ArrayList<>();
        purchaseFrequency.add(createGroup("未购买", userDataCache.stream()
                .filter(data -> data.getShopClickBuy() == null || data.getShopClickBuy() == 0)
                .count()));
        purchaseFrequency.add(createGroup("1次", userDataCache.stream()
                .filter(data -> data.getShopClickBuy() != null && data.getShopClickBuy() > 0 && data.getShopClickBuy() <= 0.5)
                .count()));
        purchaseFrequency.add(createGroup("2-3次", userDataCache.stream()
                .filter(data -> data.getShopClickBuy() != null && data.getShopClickBuy() > 0.5 && data.getShopClickBuy() <= 1.5)
                .count()));
        purchaseFrequency.add(createGroup("4次以上", userDataCache.stream()
                .filter(data -> data.getShopClickBuy() != null && data.getShopClickBuy() > 1.5)
                .count()));
        result.addData("purchaseFrequency", purchaseFrequency);
        
        // 10. 点击购买与实际消费关系（散点图数据）
        // 只显示有实际购买行为的用户，过滤掉金额为0的数据
        List<List<Double>> clickBuyRelation = userDataCache.stream()
                .filter(data -> data.getShopClickBuy() != null 
                        && data.getFirstOrderPrice() != null 
                        && data.getFirstOrderPrice() > 0  // 过滤掉0元订单
                        && data.getShopClickBuy() > 0)     // 过滤掉未点击购买的
                .limit(500)  // 限制数据量避免图表过于密集
                .map(data -> Arrays.asList(data.getShopClickBuy(), data.getFirstOrderPrice()))
                .collect(Collectors.toList());
        result.addData("clickBuyRelation", clickBuyRelation);
        
        return result;
    }
    
    @Override
    public AnalysisResult getUserProfileAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 1. 总用户数
        result.addData("totalUsers", (long) userDataCache.size());
        
        // 2. 城市分布 TOP 20（转换为前端需要的格式，并转换为中文）
        Map<String, Long> cityDist = getCityDistribution();
        List<Map<String, Object>> topCities = cityDist.entrySet().stream()
                .limit(20)
                .map(e -> {
                    Map<String, Object> city = new HashMap<>();
                    city.put("name", CityNameMapper.toChinese(e.getKey()));
                    city.put("value", e.getValue());
                    return city;
                })
                .collect(Collectors.toList());
        result.addData("topCities", topCities);
        
        // 3. 账户年龄分布
        Map<String, Long> ageDist = getAgeMonthDistribution();
        List<String> ageRanges = new ArrayList<>(ageDist.keySet());
        List<Long> ageCounts = new ArrayList<>(ageDist.values());
        result.addData("ageRanges", ageRanges);
        result.addData("ageCounts", ageCounts);
        
        // 4. 设备型号分布（饼图格式）
        Map<String, Long> deviceDist = getDeviceDistribution();
        List<Map<String, Object>> deviceGroups = deviceDist.entrySet().stream()
                .map(e -> {
                    Map<String, Object> device = new HashMap<>();
                    device.put("name", e.getKey());
                    device.put("value", e.getValue());
                    return device;
                })
                .collect(Collectors.toList());
        result.addData("deviceGroups", deviceGroups);
        
        // 5. 平台分布（环形图格式）
        Map<String, Long> platformCount = userDataCache.stream()
                .filter(data -> data.getPlatformNum() != null)
                .collect(Collectors.groupingBy(
                        data -> String.format("平台%.1f", data.getPlatformNum()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        
        List<Map<String, Object>> platformGroups = platformCount.entrySet().stream()
                .map(e -> {
                    Map<String, Object> platform = new HashMap<>();
                    platform.put("name", e.getKey());
                    platform.put("value", e.getValue());
                    return platform;
                })
                .collect(Collectors.toList());
        result.addData("platformGroups", platformGroups);
        
        // 6. 用户综合画像雷达图（6个维度，归一化到0-100）
        // 活跃度（登录天数）
        double avgLoginDay = userDataCache.stream()
                .filter(data -> data.getLoginDay() != null)
                .mapToDouble(UserBehaviorData::getLoginDay)
                .average().orElse(0.0);
        double activityScore = Math.min(avgLoginDay / 60.0 * 100, 100);
        
        // 消费力（首单金额）
        double avgOrderPrice = userDataCache.stream()
                .filter(data -> data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .mapToDouble(UserBehaviorData::getFirstOrderPrice)
                .average().orElse(0.0);
        double consumptionScore = Math.min(avgOrderPrice / 300.0 * 100, 100);
        
        // 互动性（社交行为）
        double avgSocial = userDataCache.stream()
                .filter(data -> data.getAddFriend() != null && data.getAddGroup() != null)
                .mapToDouble(data -> (data.getAddFriend() + data.getAddGroup()) / 2.0)
                .average().orElse(0.0);
        double interactionScore = avgSocial * 100;
        
        // 粘性（访问频次）
        double avgVisit = userDataCache.stream()
                .filter(data -> data.getVisitNum() != null)
                .mapToDouble(UserBehaviorData::getVisitNum)
                .average().orElse(0.0);
        double stickinessScore = Math.min(avgVisit / 8.0 * 100, 100);
        
        // 探索性（浏览深度）
        double avgSearch = userDataCache.stream()
                .filter(data -> data.getSearchNum() != null)
                .mapToDouble(UserBehaviorData::getSearchNum)
                .average().orElse(0.0);
        double explorationScore = Math.min(avgSearch / 40.0 * 100, 100);
        
        // 忠诚度（使用时长）
        double avgLoginTime = userDataCache.stream()
                .filter(data -> data.getLoginTime() != null)
                .mapToDouble(UserBehaviorData::getLoginTime)
                .average().orElse(0.0);
        double loyaltyScore = Math.min(avgLoginTime / 300.0 * 100, 100);
        
        List<Double> userRadar = Arrays.asList(
                round(activityScore, 0),
                round(consumptionScore, 0),
                round(interactionScore, 0),
                round(stickinessScore, 0),
                round(explorationScore, 0),
                round(loyaltyScore, 0)
        );
        result.addData("userRadar", userRadar);
        
        // 7. 活跃度热力图（8个时段 x 7天）
        List<String> activityHours = Arrays.asList("0-3时", "3-6时", "6-9时", "9-12时", "12-15时", "15-18时", "18-21时", "21-24时");
        result.addData("activityHours", activityHours);
        
        // 生成模拟的热力图数据（实际应基于真实时段数据）
        List<List<Integer>> activityHeatmap = new ArrayList<>();
        for (int day = 0; day < 7; day++) {
            for (int hour = 0; hour < 8; hour++) {
                // 模拟：工作日白天活跃，周末晚上活跃
                int baseActivity = 30;
                if (day < 5) { // 工作日
                    if (hour >= 3 && hour <= 5) baseActivity = 60; // 9-18时
                } else { // 周末
                    if (hour >= 4 && hour <= 7) baseActivity = 70; // 12时-24时
                }
                int randomVariation = (int)(Math.random() * 20);
                activityHeatmap.add(Arrays.asList(hour, day, baseActivity + randomVariation));
            }
        }
        result.addData("activityHeatmap", activityHeatmap);
        
        // 8. 用户分类象限图（活跃度 vs 消费力）
        // 优化：使用更细的分组，并添加随机扰动避免重叠
        Map<String, List<Double>> userScores = new HashMap<>();
        Random random = new Random();
        
        for (UserBehaviorData user : userDataCache) {
            // 活跃度得分（基于登录天数，0-100）
            double userActivityScore = 0;
            if (user.getLoginDay() != null && user.getLoginDay() > 0) {
                userActivityScore = Math.min(user.getLoginDay() / 60.0 * 100, 100);
            }
            
            // 消费力得分（基于首单金额，0-100）
            double userConsumptionScore = 0;
            if (user.getFirstOrderPrice() != null && user.getFirstOrderPrice() > 0) {
                userConsumptionScore = Math.min(user.getFirstOrderPrice() / 300.0 * 100, 100);
            }
            
            // 跳过无效数据点（两个维度都为0的）
            if (userActivityScore == 0 && userConsumptionScore == 0) {
                continue;
            }
            
            // 将得分分组（每15分一组，并添加随机扰动避免完全重叠）
            int activityGroup = (int)(userActivityScore / 15) * 15 + 7; // 中心点
            int consumptionGroup = (int)(userConsumptionScore / 15) * 15 + 7;
            
            // 添加小范围随机扰动（±5）使数据点分散
            double activityWithJitter = activityGroup + (random.nextDouble() * 10 - 5);
            double consumptionWithJitter = consumptionGroup + (random.nextDouble() * 10 - 5);
            
            // 限制在0-100范围内
            activityWithJitter = Math.max(0, Math.min(100, activityWithJitter));
            consumptionWithJitter = Math.max(0, Math.min(100, consumptionWithJitter));
            
            String key = activityGroup + "," + consumptionGroup;
            
            if (!userScores.containsKey(key)) {
                userScores.put(key, Arrays.asList(activityWithJitter, consumptionWithJitter, 1.0));
            } else {
                List<Double> current = userScores.get(key);
                // 保持位置，只增加用户数
                userScores.put(key, Arrays.asList(current.get(0), current.get(1), current.get(2) + 1));
            }
        }
        
        // 转换为列表，并过滤掉用户数太少的点
        List<List<Double>> userSegmentation = userScores.values().stream()
                .filter(point -> point.get(2) >= 5) // 至少5个用户才显示
                .collect(Collectors.toList());
        
        result.addData("userSegmentation", userSegmentation);
        
        return result;
    }
    
    @Override
    public Map<String, Long> getCityDistribution() {
        return userDataCache.stream()
                .filter(data -> data.getCityNum() != null && !data.getCityNum().isEmpty())
                .collect(Collectors.groupingBy(
                        UserBehaviorData::getCityNum,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        e -> getCityChineseName(e.getKey()),  // 转换为中文名
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    @Override
    public Map<String, Long> getAgeMonthDistribution() {
        Map<String, Long> distribution = new LinkedHashMap<>();
        
        distribution.put("0-6月", countByAgeMonth(0, 6));
        distribution.put("6-12月", countByAgeMonth(6, 12));
        distribution.put("12-24月", countByAgeMonth(12, 24));
        distribution.put("24月以上", countByAgeMonth(24, Double.MAX_VALUE));
        
        return distribution;
    }
    
    @Override
    public Map<String, Long> getDeviceDistribution() {
        return userDataCache.stream()
                .filter(data -> data.getModelNum() != null)
                .collect(Collectors.groupingBy(
                        data -> String.format("设备%.0f", data.getModelNum()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    @Override
    public AnalysisResult getShopCartConversionAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 使用过购物车的用户
        long cartUsers = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0)
                .count();
        
        // 平均购物车访问次数
        double avgCartVisit = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null)
                .mapToDouble(UserBehaviorData::getShopCartPage)
                .average()
                .orElse(0.0);
        
        // 购物车转化用户 (访问购物车且有首单)
        long cartConversionUsers = userDataCache.stream()
                .filter(data -> data.getShopCartPage() != null && data.getShopCartPage() > 0
                        && data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .count();
        
        // 转化率
        double conversionRate = cartUsers > 0 ?
                round((double) cartConversionUsers / cartUsers * 100, 2) : 0.0;
        
        result.addData("cartUsers", cartUsers);
        result.addData("avgCartVisit", round(avgCartVisit, 2));
        result.addData("cartConversionUsers", cartConversionUsers);
        result.addData("conversionRate", conversionRate);
        
        return result;
    }
    
    @Override
    public AnalysisResult getCouponUsageAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 领取优惠券的用户
        long couponUsers = userDataCache.stream()
                .filter(data -> data.getCouponNum() != null && data.getCouponNum() > 0)
                .count();
        
        // 平均优惠券数量
        double avgCouponNum = userDataCache.stream()
                .filter(data -> data.getCouponNum() != null)
                .mapToDouble(UserBehaviorData::getCouponNum)
                .average()
                .orElse(0.0);
        
        // 访问优惠券页面的用户
        long couponVisitUsers = userDataCache.stream()
                .filter(data -> data.getCouponVisit() != null && data.getCouponVisit() > 0)
                .count();
        
        result.addData("couponUsers", couponUsers);
        result.addData("avgCouponNum", round(avgCouponNum, 2));
        result.addData("couponVisitUsers", couponVisitUsers);
        result.addData("couponUsageRate", 
                round((double) couponUsers / userDataCache.size() * 100, 2));
        
        return result;
    }
    
    @Override
    public AnalysisResult getSocialBehaviorAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 1. 平均好友数（addFriend字段，1表示添加了好友，0表示没有）
        double avgFriends = userDataCache.stream()
                .filter(data -> data.getAddFriend() != null && data.getAddFriend() > 0)
                .count();
        avgFriends = avgFriends / userDataCache.size();
        result.addData("avgFriends", round(avgFriends, 2));
        
        // 2. 平均群组数（addGroup字段，1表示加入了群组，0表示没有）
        double avgGroups = userDataCache.stream()
                .filter(data -> data.getAddGroup() != null && data.getAddGroup() > 0)
                .count();
        avgGroups = avgGroups / userDataCache.size();
        result.addData("avgGroups", round(avgGroups, 2));
        
        // 3. 平均分享次数
        double avgShares = userDataCache.stream()
                .filter(data -> data.getShare() != null)
                .mapToDouble(UserBehaviorData::getShare)
                .average()
                .orElse(0.0);
        result.addData("avgShares", round(avgShares, 2));
        
        // 4. 社交参与率（至少有一种社交行为的用户比例）
        long socialUsers = userDataCache.stream()
                .filter(data -> (data.getAddFriend() != null && data.getAddFriend() > 0) ||
                               (data.getAddGroup() != null && data.getAddGroup() > 0) ||
                               (data.getShare() != null && data.getShare() > 0))
                .count();
        double socialRate = (double) socialUsers / userDataCache.size() * 100;
        result.addData("socialRate", round(socialRate, 2));
        
        // 5. 好友数量分布
        long noFriends = userDataCache.stream()
                .filter(data -> data.getAddFriend() == null || data.getAddFriend() == 0)
                .count();
        long hasFriends = userDataCache.size() - noFriends;
        
        List<String> friendsRanges = Arrays.asList("未添加好友", "已添加好友");
        List<Long> friendsCounts = Arrays.asList(noFriends, hasFriends);
        result.addData("friendsRanges", friendsRanges);
        result.addData("friendsCounts", friendsCounts);
        
        // 6. 群组数量分布
        long noGroups = userDataCache.stream()
                .filter(data -> data.getAddGroup() == null || data.getAddGroup() == 0)
                .count();
        long hasGroups = userDataCache.size() - noGroups;
        
        List<String> groupsRanges = Arrays.asList("未加入群组", "已加入群组");
        List<Long> groupsCounts = Arrays.asList(noGroups, hasGroups);
        result.addData("groupsRanges", groupsRanges);
        result.addData("groupsCounts", groupsCounts);
        
        // 7. 社交活跃用户和普通用户的雷达图对比
        // 分为社交活跃用户和普通用户两组
        List<UserBehaviorData> activeUsers = userDataCache.stream()
                .filter(data -> (data.getAddFriend() != null && data.getAddFriend() > 0) &&
                               (data.getAddGroup() != null && data.getAddGroup() > 0) &&
                               (data.getShare() != null && data.getShare() > 5))
                .collect(Collectors.toList());
        
        List<UserBehaviorData> normalUsers = userDataCache.stream()
                .filter(data -> !activeUsers.contains(data))
                .collect(Collectors.toList());
        
        // 活跃用户社交行为
        double activeAddFriend = activeUsers.isEmpty() ? 0 : 100.0;
        double activeAddGroup = activeUsers.isEmpty() ? 0 : 100.0;
        double activeAvgShare = activeUsers.stream()
                .filter(d -> d.getShare() != null)
                .mapToDouble(UserBehaviorData::getShare)
                .average().orElse(0.0);
        activeAvgShare = Math.min(activeAvgShare / 30.0 * 100, 100);
        
        double activeAvgSubscribe = activeUsers.stream()
                .filter(d -> d.getSubscribeNum1() != null)
                .mapToDouble(UserBehaviorData::getSubscribeNum1)
                .average().orElse(0.0);
        activeAvgSubscribe = Math.min(activeAvgSubscribe / 10.0 * 100, 100);
        
        double activeAvgDialog = activeUsers.stream()
                .filter(d -> d.getClickDialog() != null && d.getClickDialog() > 0)
                .count() * 100.0 / Math.max(activeUsers.size(), 1);
        
        List<Double> activeUserRadar = Arrays.asList(
                round(activeAddFriend, 0),
                round(activeAddGroup, 0),
                round(activeAvgShare, 0),
                round(activeAvgSubscribe, 0),
                round(activeAvgDialog, 0)
        );
        result.addData("activeUserRadar", activeUserRadar);
        
        // 普通用户社交行为
        double normalAddFriendRate = normalUsers.stream()
                .filter(d -> d.getAddFriend() != null && d.getAddFriend() > 0)
                .count() * 100.0 / Math.max(normalUsers.size(), 1);
        
        double normalAddGroupRate = normalUsers.stream()
                .filter(d -> d.getAddGroup() != null && d.getAddGroup() > 0)
                .count() * 100.0 / Math.max(normalUsers.size(), 1);
        
        double normalAvgShare = normalUsers.stream()
                .filter(d -> d.getShare() != null)
                .mapToDouble(UserBehaviorData::getShare)
                .average().orElse(0.0);
        normalAvgShare = Math.min(normalAvgShare / 30.0 * 100, 100);
        
        double normalAvgSubscribe = normalUsers.stream()
                .filter(d -> d.getSubscribeNum1() != null)
                .mapToDouble(UserBehaviorData::getSubscribeNum1)
                .average().orElse(0.0);
        normalAvgSubscribe = Math.min(normalAvgSubscribe / 10.0 * 100, 100);
        
        double normalAvgDialog = normalUsers.stream()
                .filter(d -> d.getClickDialog() != null && d.getClickDialog() > 0)
                .count() * 100.0 / Math.max(normalUsers.size(), 1);
        
        List<Double> normalUserRadar = Arrays.asList(
                round(normalAddFriendRate, 0),
                round(normalAddGroupRate, 0),
                round(normalAvgShare, 0),
                round(normalAvgSubscribe, 0),
                round(normalAvgDialog, 0)
        );
        result.addData("normalUserRadar", normalUserRadar);
        
        // 8. 分享频次分布
        List<Map<String, Object>> shareGroups = new ArrayList<>();
        shareGroups.add(createGroup("未分享", userDataCache.stream()
                .filter(d -> d.getShare() == null || d.getShare() == 0)
                .count()));
        shareGroups.add(createGroup("1-5次", userDataCache.stream()
                .filter(d -> d.getShare() != null && d.getShare() > 0 && d.getShare() <= 5)
                .count()));
        shareGroups.add(createGroup("6-15次", userDataCache.stream()
                .filter(d -> d.getShare() != null && d.getShare() > 5 && d.getShare() <= 15)
                .count()));
        shareGroups.add(createGroup("16-30次", userDataCache.stream()
                .filter(d -> d.getShare() != null && d.getShare() > 15 && d.getShare() <= 30)
                .count()));
        shareGroups.add(createGroup("30次以上", userDataCache.stream()
                .filter(d -> d.getShare() != null && d.getShare() > 30)
                .count()));
        result.addData("shareGroups", shareGroups);
        
        // 9. 订阅行为分布
        List<String> subscribeRanges = Arrays.asList("0个", "1-3个", "4-7个", "7个以上");
        List<Long> subscribeCounts = Arrays.asList(
                userDataCache.stream().filter(d -> d.getSubscribeNum1() == null || d.getSubscribeNum1() == 0).count(),
                userDataCache.stream().filter(d -> d.getSubscribeNum1() != null && d.getSubscribeNum1() > 0 && d.getSubscribeNum1() <= 3).count(),
                userDataCache.stream().filter(d -> d.getSubscribeNum1() != null && d.getSubscribeNum1() > 3 && d.getSubscribeNum1() <= 7).count(),
                userDataCache.stream().filter(d -> d.getSubscribeNum1() != null && d.getSubscribeNum1() > 7).count()
        );
        result.addData("subscribeRanges", subscribeRanges);
        result.addData("subscribeCounts", subscribeCounts);
        
        // 10. 社交关系强度与活跃度散点图
        // 计算综合社交强度（包含多个维度）并添加随机扰动
        java.util.Random random = new java.util.Random(42); // 固定种子保证可重复
        List<List<Double>> socialStrengthData = userDataCache.stream()
                .filter(d -> d.getAddFriend() != null && d.getAddGroup() != null 
                        && d.getLoginDay() != null && d.getShare() != null
                        && d.getSubscribeNum1() != null)
                .limit(800)  // 增加采样数量
                .map(d -> {
                    // 综合社交强度计算（多维度，范围0-100）
                    double baseSocial = (d.getAddFriend() + d.getAddGroup()) * 15; // 0-30分
                    double shareScore = Math.min(d.getShare() / 30.0 * 35, 35);    // 0-35分
                    double subscribeScore = Math.min(d.getSubscribeNum1() / 10.0 * 35, 35); // 0-35分
                    
                    double socialStrength = baseSocial + shareScore + subscribeScore;
                    
                    // 添加较大范围随机扰动（±8）使数据点更分散
                    socialStrength += (random.nextDouble() * 16 - 8);
                    socialStrength = Math.max(5, Math.min(95, socialStrength)); // 限制在5-95，避免边界
                    
                    // 活跃度（登录天数）
                    double activity = d.getLoginDay();
                    
                    return Arrays.asList(socialStrength, activity);
                })
                .collect(Collectors.toList());
        result.addData("socialStrengthData", socialStrengthData);
        
        return result;
    }
    
    @Override
    public AnalysisResult getContentPreferenceAnalysis() {
        AnalysisResult result = new AnalysisResult();
        
        // 平均内容阅读次数
        double avgContentRead = userDataCache.stream()
                .filter(data -> data.getContentRead() != null)
                .mapToDouble(UserBehaviorData::getContentRead)
                .average()
                .orElse(0.0);
        
        // 平均内容收藏次数
        double avgContentCollect = userDataCache.stream()
                .filter(data -> data.getContentCollect() != null)
                .mapToDouble(UserBehaviorData::getContentCollect)
                .average()
                .orElse(0.0);
        
        // 前端字段名 (注意：avgReadCount不是avgContentRead)
        result.addData("avgReadCount", round(avgContentRead, 2));
        result.addData("avgCollectCount", round(avgContentCollect, 2));
        
        // 分类范围
        List<String> categories = Arrays.asList("0-5次", "6-10次", "11-20次", "21-50次", "51-100次", "100+次");
        result.addData("categories", categories);
        result.addData("readRanges", categories);
        result.addData("collectRanges", categories);
        
        // 阅读次数分布统计
        List<Long> readCounts = Arrays.asList(
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() >= 0 && d.getContentRead() <= 5).count(),
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 5 && d.getContentRead() <= 10).count(),
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 10 && d.getContentRead() <= 20).count(),
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 20 && d.getContentRead() <= 50).count(),
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 50 && d.getContentRead() <= 100).count(),
                userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 100).count()
        );
        result.addData("readCounts", readCounts);
        result.addData("readDistribution", readCounts);
        
        // 收藏次数分布统计
        List<Long> collectCounts = Arrays.asList(
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() >= 0 && d.getContentCollect() <= 5).count(),
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 5 && d.getContentCollect() <= 10).count(),
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 10 && d.getContentCollect() <= 20).count(),
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 20 && d.getContentCollect() <= 50).count(),
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 50 && d.getContentCollect() <= 100).count(),
                userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 100).count()
        );
        result.addData("collectCounts", collectCounts);
        result.addData("collectDistribution", collectCounts);
        
        // 收藏转化率 (每个区间的收藏次数/阅读次数 * 100)
        List<Double> collectRates = new ArrayList<>();
        for (int i = 0; i < readCounts.size(); i++) {
            double rate = readCounts.get(i) > 0 ? (collectCounts.get(i) * 100.0 / readCounts.get(i)) : 0.0;
            collectRates.add(round(rate, 2));
        }
        result.addData("collectRates", collectRates);
        
        // 内容偏好雷达图数据 (归一化到0-100)
        double maxRead = Math.max(1.0, userDataCache.stream().filter(d -> d.getContentRead() != null).mapToDouble(UserBehaviorData::getContentRead).max().orElse(1.0));
        double maxCollect = Math.max(1.0, userDataCache.stream().filter(d -> d.getContentCollect() != null).mapToDouble(UserBehaviorData::getContentCollect).max().orElse(1.0));
        double maxTask = Math.max(1.0, userDataCache.stream().filter(d -> d.getTask() != null).mapToDouble(UserBehaviorData::getTask).max().orElse(1.0));
        double maxRecommend = Math.max(1.0, userDataCache.stream().filter(d -> d.getRecommendVisit() != null).mapToDouble(UserBehaviorData::getRecommendVisit).max().orElse(1.0));
        double maxShare = Math.max(1.0, userDataCache.stream().filter(d -> d.getShare() != null).mapToDouble(UserBehaviorData::getShare).max().orElse(1.0));
        
        List<Double> preferenceRadar = Arrays.asList(
                round((avgContentRead / maxRead) * 100, 2),
                round((avgContentCollect / maxCollect) * 100, 2),
                round((userDataCache.stream().filter(d -> d.getRecommendVisit() != null).mapToDouble(UserBehaviorData::getRecommendVisit).average().orElse(0.0) / maxRecommend) * 100, 2),
                round((userDataCache.stream().filter(d -> d.getSaleTab() != null).mapToDouble(UserBehaviorData::getSaleTab).average().orElse(0.0) / Math.max(1.0, userDataCache.stream().filter(d -> d.getSaleTab() != null).mapToDouble(UserBehaviorData::getSaleTab).max().orElse(1.0))) * 100, 2),
                round((userDataCache.stream().filter(d -> d.getTask() != null).mapToDouble(UserBehaviorData::getTask).average().orElse(0.0) / maxTask) * 100, 2)
        );
        result.addData("preferenceRadar", preferenceRadar);
        result.addData("contentRadar", preferenceRadar); // 前端期待的字段名
        
        // 漏斗图数据
        long totalUsers = userDataCache.size();
        long readUsers = userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 0).count();
        long activeReadUsers = userDataCache.stream().filter(d -> d.getContentRead() != null && d.getContentRead() > 10).count();
        long collectUsers = userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 0).count();
        long loyalUsers = userDataCache.stream().filter(d -> d.getContentCollect() != null && d.getContentCollect() > 20).count();
        
        List<Map<String, Object>> funnelData = new ArrayList<>();
        funnelData.add(createFunnelData("浏览内容", totalUsers));
        funnelData.add(createFunnelData("开始阅读", readUsers));
        funnelData.add(createFunnelData("深度阅读", activeReadUsers));
        funnelData.add(createFunnelData("点击收藏", collectUsers));
        funnelData.add(createFunnelData("忠实用户", loyalUsers));
        result.addData("funnelData", funnelData);
        result.addData("readCollectFunnel", funnelData); // 前端期待的字段名
        
        // 收藏率分布
        List<String> rateRanges = Arrays.asList("0-20%", "20-40%", "40-60%", "60-80%", "80-100%");
        List<Long> rateCounts = Arrays.asList(
                userDataCache.stream().filter(d -> calculateCollectRate(d) >= 0 && calculateCollectRate(d) <= 20).count(),
                userDataCache.stream().filter(d -> calculateCollectRate(d) > 20 && calculateCollectRate(d) <= 40).count(),
                userDataCache.stream().filter(d -> calculateCollectRate(d) > 40 && calculateCollectRate(d) <= 60).count(),
                userDataCache.stream().filter(d -> calculateCollectRate(d) > 60 && calculateCollectRate(d) <= 80).count(),
                userDataCache.stream().filter(d -> calculateCollectRate(d) > 80 && calculateCollectRate(d) <= 100).count()
        );
        result.addData("rateRanges", rateRanges);
        result.addData("rateDistribution", rateCounts);
        
        // 前端期待的格式：{name, value}
        List<Map<String, Object>> collectRateGroups = new ArrayList<>();
        for (int i = 0; i < rateRanges.size(); i++) {
            Map<String, Object> group = new HashMap<>();
            group.put("name", rateRanges.get(i));
            group.put("value", rateCounts.get(i));
            collectRateGroups.add(group);
        }
        result.addData("collectRateGroups", collectRateGroups);
        
        // 散点图数据 (阅读次数 vs 收藏次数)
        List<List<Double>> scatterData = userDataCache.stream()
                .filter(d -> d.getContentRead() != null && d.getContentCollect() != null)
                .filter(d -> d.getContentRead() > 0 || d.getContentCollect() > 0) // 过滤掉都为0的
                .limit(1000) // 限制数据量，避免过多
                .map(d -> Arrays.asList(d.getContentRead(), d.getContentCollect()))
                .collect(Collectors.toList());
        result.addData("scatterData", scatterData);
        result.addData("readCollectRelation", scatterData); // 前端期待的字段名
        
        return result;
    }
    
    /**
     * 计算用户的收藏率 (收藏次数/阅读次数*100)
     */
    private double calculateCollectRate(UserBehaviorData data) {
        if (data.getContentRead() == null || data.getContentRead() == 0) {
            return 0.0;
        }
        if (data.getContentCollect() == null) {
            return 0.0;
        }
        return (data.getContentCollect() / data.getContentRead()) * 100;
    }
    
    // ========== 私有辅助方法 ==========
    
    /**
     * 统计登录天数范围内的用户数
     */
    private long countByLoginDays(int min, int max) {
        return userDataCache.stream()
                .filter(data -> data.getLoginDay() != null)
                .filter(data -> {
                    double days = data.getLoginDay();
                    return days >= min && days < max;
                })
                .count();
    }
    
    /**
     * 统计登录时长范围内的用户数
     */
    private long countByLoginTime(double min, double max) {
        return userDataCache.stream()
                .filter(data -> data.getLoginTime() != null)
                .filter(data -> {
                    double time = data.getLoginTime();
                    return time >= min && time < max;
                })
                .count();
    }
    
    /**
     * 统计账户年龄范围内的用户数
     */
    private long countByAgeMonth(double min, double max) {
        return userDataCache.stream()
                .filter(data -> data.getAgeMonth() != null)
                .filter(data -> {
                    double age = data.getAgeMonth();
                    return age >= min && age < max;
                })
                .count();
    }
    
    /**
     * 统计首单金额范围内的用户数
     */
    private long countByOrderPrice(double min, double max) {
        return userDataCache.stream()
                .filter(data -> data.getFirstOrderPrice() != null && data.getFirstOrderPrice() > 0)
                .filter(data -> {
                    double price = data.getFirstOrderPrice();
                    return price >= min && price < max;
                })
                .count();
    }
    
    /**
     * 统计详情页访问次数范围内的用户数
     */
    private long countByDetailNum(double min, double max) {
        return userDataCache.stream()
                .filter(data -> data.getDetailNum() != null)
                .filter(data -> {
                    double detail = data.getDetailNum();
                    return detail >= min && detail < max;
                })
                .count();
    }
    
    /**
     * 统计搜索次数范围内的用户数
     */
    private long countBySearchNum(double min, double max) {
        return userDataCache.stream()
                .filter(data -> data.getSearchNum() != null)
                .filter(data -> {
                    double search = data.getSearchNum();
                    return search >= min && search < max;
                })
                .count();
    }
    
    /**
     * 创建漏斗图数据
     */
    private Map<String, Object> createFunnelData(String name, double value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }
    
    /**
     * 四舍五入保留指定小数位
     */
    private double round(double value, int places) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

