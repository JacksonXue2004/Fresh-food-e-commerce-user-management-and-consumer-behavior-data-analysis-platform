package com.ruoyi.freshdata.service;

import com.ruoyi.freshdata.domain.AnalysisResult;
import com.ruoyi.freshdata.domain.UserBehaviorData;

import java.util.List;
import java.util.Map;

/**
 * 数据分析服务接口
 * 
 * @author ruoyi
 */
public interface IDataAnalysisService {
    
    /**
     * 获取所有用户行为数据
     * 
     * @return 用户行为数据列表
     */
    List<UserBehaviorData> getAllUserData();
    
    /**
     * 获取数据概览统计
     * 
     * @return 分析结果
     */
    AnalysisResult getDataOverview();
    
    /**
     * 获取用户活跃度分布
     * 
     * @return Map<活跃度, 用户数>
     */
    Map<String, Long> getUserActiveDistribution();
    
    /**
     * 获取登录行为分析
     * 
     * @return 分析结果
     */
    AnalysisResult getLoginBehaviorAnalysis();
    
    /**
     * 获取浏览行为分析
     * 
     * @return 分析结果
     */
    AnalysisResult getBrowseBehaviorAnalysis();
    
    /**
     * 获取消费行为分析
     * 
     * @return 分析结果
     */
    AnalysisResult getConsumptionAnalysis();
    
    /**
     * 获取用户画像分析
     * 
     * @return 分析结果
     */
    AnalysisResult getUserProfileAnalysis();
    
    /**
     * 获取地域分布分析
     * 
     * @return Map<城市, 用户数>
     */
    Map<String, Long> getCityDistribution();
    
    /**
     * 获取账户年龄分布
     * 
     * @return Map<年龄段, 用户数>
     */
    Map<String, Long> getAgeMonthDistribution();
    
    /**
     * 获取设备型号分布 TOP 10
     * 
     * @return Map<设备型号, 用户数>
     */
    Map<String, Long> getDeviceDistribution();
    
    /**
     * 获取购物车转化分析
     * 
     * @return 分析结果
     */
    AnalysisResult getShopCartConversionAnalysis();
    
    /**
     * 获取优惠券使用分析
     * 
     * @return 分析结果
     */
    AnalysisResult getCouponUsageAnalysis();
    
    /**
     * 获取社交行为分析
     * 
     * @return 分析结果
     */
    AnalysisResult getSocialBehaviorAnalysis();
    
    /**
     * 获取内容偏好分析
     * 
     * @return 分析结果
     */
    AnalysisResult getContentPreferenceAnalysis();
    
    /**
     * 刷新数据缓存
     */
    void refreshDataCache();
}






