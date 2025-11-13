package com.ruoyi.freshdata.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据分析结果封装类
 * 
 * @author ruoyi
 */
public class AnalysisResult {
    
    /** 总用户数 */
    private Long totalUsers;
    
    /** 活跃用户数 */
    private Long activeUsers;
    
    /** 不活跃用户数 */
    private Long inactiveUsers;
    
    /** 活跃率 */
    private Double activeRate;
    
    /** 平均登录天数 */
    private Double avgLoginDays;
    
    /** 平均登录时长 */
    private Double avgLoginTime;
    
    /** 平均首单金额 */
    private Double avgFirstOrderPrice;
    
    /** 有首单用户数 */
    private Long usersWithOrder;
    
    /** 转化率 */
    private Double conversionRate;
    
    /** 额外数据 */
    private Map<String, Object> extraData = new HashMap<>();
    
    /** 通用数据字段 */
    private Map<String, Object> data = new HashMap<>();

    public AnalysisResult() {
    }

    // Getter and Setter

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Long getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(Long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public Double getActiveRate() {
        return activeRate;
    }

    public void setActiveRate(Double activeRate) {
        this.activeRate = activeRate;
    }

    public Double getAvgLoginDays() {
        return avgLoginDays;
    }

    public void setAvgLoginDays(Double avgLoginDays) {
        this.avgLoginDays = avgLoginDays;
    }

    public Double getAvgLoginTime() {
        return avgLoginTime;
    }

    public void setAvgLoginTime(Double avgLoginTime) {
        this.avgLoginTime = avgLoginTime;
    }

    public Double getAvgFirstOrderPrice() {
        return avgFirstOrderPrice;
    }

    public void setAvgFirstOrderPrice(Double avgFirstOrderPrice) {
        this.avgFirstOrderPrice = avgFirstOrderPrice;
    }

    public Long getUsersWithOrder() {
        return usersWithOrder;
    }

    public void setUsersWithOrder(Long usersWithOrder) {
        this.usersWithOrder = usersWithOrder;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }
    
    public void addExtraData(String key, Object value) {
        this.extraData.put(key, value);
    }
    
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }
}

