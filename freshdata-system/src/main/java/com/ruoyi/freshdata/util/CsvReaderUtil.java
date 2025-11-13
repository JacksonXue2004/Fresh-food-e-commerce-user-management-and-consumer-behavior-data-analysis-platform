package com.ruoyi.freshdata.util;

import com.ruoyi.freshdata.domain.UserBehaviorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV文件读取工具类
 * 
 * @author ruoyi
 */
public class CsvReaderUtil {
    
    private static final Logger log = LoggerFactory.getLogger(CsvReaderUtil.class);
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 读取CSV文件数据
     * 
     * @param fileName 文件名
     * @return 用户行为数据列表
     */
    public static List<UserBehaviorData> readCsvData(String fileName) {
        List<UserBehaviorData> dataList = new ArrayList<>();
        
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );
            
            // 跳过表头
            String header = reader.readLine();
            log.info("CSV表头: {}", header);
            
            String line;
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    UserBehaviorData data = parseLine(line);
                    if (data != null) {
                        dataList.add(data);
                    }
                } catch (Exception e) {
                    log.warn("解析第{}行数据失败: {}, 错误: {}", lineNumber, line, e.getMessage());
                }
            }
            
            reader.close();
            log.info("成功读取CSV数据，共{}条记录", dataList.size());
            
        } catch (IOException e) {
            log.error("读取CSV文件失败: {}", e.getMessage(), e);
        }
        
        return dataList;
    }
    
    /**
     * 解析CSV行数据
     * 
     * @param line CSV行
     * @return 用户行为数据对象
     */
    private static UserBehaviorData parseLine(String line) throws ParseException {
        String[] fields = line.split(",", -1);
        
        if (fields.length < 37) {
            log.warn("数据列数不足: {}", fields.length);
            return null;
        }
        
        UserBehaviorData data = new UserBehaviorData();
        
        try {
            data.setUserId(fields[0]);
            data.setFirstOrderTime(parseDate(fields[1]));
            data.setFirstOrderPrice(parseDouble(fields[2]));
            data.setAgeMonth(parseDouble(fields[3]));
            data.setCityNum(fields[4]);
            data.setPlatformNum(parseDouble(fields[5]));
            data.setModelNum(parseDouble(fields[6]));
            data.setLoginDay(parseDouble(fields[7]));
            data.setLoginDiffTime(parseDouble(fields[8]));
            data.setDistanceDay(parseDouble(fields[9]));
            data.setLoginTime(parseDouble(fields[10]));
            data.setLaunchTime(parseDouble(fields[11]));
            data.setSubscribeNum1(parseDouble(fields[12]));
            data.setAddFriend(parseDouble(fields[13]));
            data.setAddGroup(parseDouble(fields[14]));
            data.setVisitNum(parseDouble(fields[15]));
            data.setSearchNum(parseDouble(fields[16]));
            data.setFinishNum(parseDouble(fields[17]));
            data.setDetailNum(parseDouble(fields[18]));
            data.setCouponNum(parseDouble(fields[19]));
            data.setMainHome(parseDouble(fields[20]));
            data.setMainHome2(parseDouble(fields[21]));
            data.setMainPage(parseDouble(fields[22]));
            data.setShopCartPage(parseDouble(fields[23]));
            data.setMainMime(parseDouble(fields[24]));
            data.setCollectionTab(parseDouble(fields[25]));
            data.setRecommendVisit(parseDouble(fields[26]));
            data.setCouponVisit(parseDouble(fields[27]));
            data.setShopClickBuy(parseDouble(fields[28]));
            data.setTask(parseDouble(fields[29]));
            data.setContentRead(parseDouble(fields[30]));
            data.setContentCollect(parseDouble(fields[31]));
            data.setSaleTab(parseDouble(fields[32]));
            data.setRedClick(parseDouble(fields[33]));
            data.setShare(parseDouble(fields[34]));
            data.setClickDialog(parseDouble(fields[35]));
            data.setResult(parseInt(fields[36]));
            
        } catch (Exception e) {
            log.error("解析字段失败: {}", e.getMessage());
            throw e;
        }
        
        return data;
    }
    
    /**
     * 解析Double类型
     */
    private static Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * 解析Integer类型
     */
    private static Integer parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 解析日期类型
     */
    private static java.util.Date parseDate(String value) throws ParseException {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(value.trim());
        } catch (ParseException e) {
            log.warn("日期解析失败: {}", value);
            return null;
        }
    }
}

