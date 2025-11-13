package com.ruoyi.freshdata.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 城市拼音与中文名映射工具类
 * 
 * @author ruoyi
 */
public class CityNameMapper {
    
    private static final Map<String, String> CITY_MAP = new HashMap<>();
    
    static {
        // 一线城市
        CITY_MAP.put("beijing", "北京");
        CITY_MAP.put("shanghai", "上海");
        CITY_MAP.put("guangzhou", "广州");
        CITY_MAP.put("shenzhen", "深圳");
        
        // 新一线城市
        CITY_MAP.put("chengdu", "成都");
        CITY_MAP.put("hangzhou", "杭州");
        CITY_MAP.put("chongqing", "重庆");
        CITY_MAP.put("wuhan", "武汉");
        CITY_MAP.put("xian", "西安");
        CITY_MAP.put("suzhou", "苏州");
        CITY_MAP.put("tianjin", "天津");
        CITY_MAP.put("nanjing", "南京");
        CITY_MAP.put("changsha", "长沙");
        CITY_MAP.put("zhengzhou", "郑州");
        CITY_MAP.put("dongguan", "东莞");
        CITY_MAP.put("qingdao", "青岛");
        CITY_MAP.put("shenyang", "沈阳");
        CITY_MAP.put("ningbo", "宁波");
        CITY_MAP.put("kunming", "昆明");
        CITY_MAP.put("jinan", "济南");
        
        // 其他城市
        CITY_MAP.put("dalian", "大连");
        CITY_MAP.put("xiamen", "厦门");
        CITY_MAP.put("foshan", "佛山");
        CITY_MAP.put("hefei", "合肥");
        CITY_MAP.put("fuzhou", "福州");
        CITY_MAP.put("harbin", "哈尔滨");
        CITY_MAP.put("changchun", "长春");
        CITY_MAP.put("wuxi", "无锡");
        CITY_MAP.put("shijiazhuang", "石家庄");
        CITY_MAP.put("taiyuan", "太原");
    }
    
    /**
     * 将拼音城市名转换为中文
     * 
     * @param pinyinName 拼音城市名
     * @return 中文城市名，如果找不到映射则返回原名
     */
    public static String toChinese(String pinyinName) {
        if (pinyinName == null || pinyinName.isEmpty()) {
            return pinyinName;
        }
        return CITY_MAP.getOrDefault(pinyinName.toLowerCase(), pinyinName);
    }
    
    /**
     * 批量转换城市名
     * 
     * @param pinyinNames 拼音城市名数组
     * @return 中文城市名数组
     */
    public static String[] toChinese(String[] pinyinNames) {
        if (pinyinNames == null) {
            return null;
        }
        String[] chineseNames = new String[pinyinNames.length];
        for (int i = 0; i < pinyinNames.length; i++) {
            chineseNames[i] = toChinese(pinyinNames[i]);
        }
        return chineseNames;
    }
}

