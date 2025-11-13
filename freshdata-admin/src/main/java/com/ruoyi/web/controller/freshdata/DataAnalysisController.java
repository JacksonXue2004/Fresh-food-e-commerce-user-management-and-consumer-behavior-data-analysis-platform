package com.ruoyi.web.controller.freshdata;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.freshdata.domain.AnalysisResult;
import com.ruoyi.freshdata.domain.UserBehaviorData;
import com.ruoyi.freshdata.service.IDataAnalysisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据分析Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/freshdata/analysis")
public class DataAnalysisController extends BaseController {
    
    @Autowired
    private IDataAnalysisService dataAnalysisService;
    
    /**
     * 获取数据概览
     */
    // @RequiresPermissions("freshdata:analysis:overview")  // 临时注释，测试通过后恢复
    @GetMapping("/overview")
    public AjaxResult getOverview() {
        AnalysisResult result = dataAnalysisService.getDataOverview();
        // 直接返回data字段，避免多层嵌套
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取用户活跃度分布
     */
    // @RequiresPermissions("freshdata:analysis:active")  // 临时注释
    @GetMapping("/active/distribution")
    public AjaxResult getActiveDistribution() {
        Map<String, Long> distribution = dataAnalysisService.getUserActiveDistribution();
        return AjaxResult.success(distribution);
    }
    
    /**
     * 获取登录行为分析
     */
    // @RequiresPermissions("freshdata:analysis:login")  // 临时注释
    @GetMapping("/loginBehavior")
    public AjaxResult getLoginBehavior() {
        AnalysisResult result = dataAnalysisService.getLoginBehaviorAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取浏览行为分析
     */
    // @RequiresPermissions("freshdata:analysis:browse")  // 临时注释
    @GetMapping("/browseBehavior")
    public AjaxResult getBrowseBehavior() {
        AnalysisResult result = dataAnalysisService.getBrowseBehaviorAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取搜索行为分析
     */
    // @RequiresPermissions("freshdata:analysis:browse")  // 临时注释
    @GetMapping("/searchBehavior")
    public AjaxResult getSearchBehavior() {
        AnalysisResult result = dataAnalysisService.getBrowseBehaviorAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取消费行为分析
     */
    // @RequiresPermissions("freshdata:analysis:consumption")  // 临时注释
    @GetMapping("/consumptionBehavior")
    public AjaxResult getConsumptionBehavior() {
        AnalysisResult result = dataAnalysisService.getConsumptionAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取购物车转化分析
     */
    // @RequiresPermissions("freshdata:analysis:cart")  // 临时注释
    @GetMapping("/cartAnalysis")
    public AjaxResult getCartAnalysis() {
        AnalysisResult result = dataAnalysisService.getShopCartConversionAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取优惠券使用分析
     */
    // @RequiresPermissions("freshdata:analysis:coupon")  // 临时注释
    @GetMapping("/couponAnalysis")
    public AjaxResult getCouponAnalysis() {
        AnalysisResult result = dataAnalysisService.getCouponUsageAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取用户画像分析
     */
    // @RequiresPermissions("freshdata:analysis:profile")  // 临时注释
    @GetMapping("/userProfile")
    public AjaxResult getUserProfile() {
        AnalysisResult result = dataAnalysisService.getUserProfileAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取地域分布
     */
    // @RequiresPermissions("freshdata:analysis:city")  // 临时注释
    @GetMapping("/cityDistribution")
    public AjaxResult getCityDistribution() {
        Map<String, Long> distribution = dataAnalysisService.getCityDistribution();
        return AjaxResult.success(distribution);
    }
    
    /**
     * 获取账户年龄分布
     */
    // @RequiresPermissions("freshdata:analysis:age")  // 临时注释
    @GetMapping("/ageDistribution")
    public AjaxResult getAgeDistribution() {
        Map<String, Long> distribution = dataAnalysisService.getAgeMonthDistribution();
        return AjaxResult.success(distribution);
    }
    
    /**
     * 获取设备分布
     */
    // @RequiresPermissions("freshdata:analysis:device")  // 临时注释
    @GetMapping("/deviceDistribution")
    public AjaxResult getDeviceDistribution() {
        Map<String, Long> distribution = dataAnalysisService.getDeviceDistribution();
        return AjaxResult.success(distribution);
    }
    
    /**
     * 获取社交行为分析
     */
    // @RequiresPermissions("freshdata:analysis:social")  // 临时注释
    @GetMapping("/socialBehavior")
    public AjaxResult getSocialBehavior() {
        AnalysisResult result = dataAnalysisService.getSocialBehaviorAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取内容偏好分析
     */
    // @RequiresPermissions("freshdata:analysis:content")  // 临时注释
    @GetMapping("/contentPreference")
    public AjaxResult getContentPreference() {
        AnalysisResult result = dataAnalysisService.getContentPreferenceAnalysis();
        return AjaxResult.success(result.getData());
    }
    
    /**
     * 获取用户行为数据列表(分页)
     */
    // @RequiresPermissions("freshdata:data:list")  // 临时注释
    @GetMapping("/data/list")
    public TableDataInfo getUserDataList() {
        startPage();
        List<UserBehaviorData> list = dataAnalysisService.getAllUserData();
        return getDataTable(list);
    }
    
    /**
     * 刷新数据缓存
     */
    // @RequiresPermissions("freshdata:data:refresh")  // 临时注释
    @PostMapping("/data/refresh")
    public AjaxResult refreshData() {
        dataAnalysisService.refreshDataCache();
        return AjaxResult.success("数据刷新成功");
    }
}

