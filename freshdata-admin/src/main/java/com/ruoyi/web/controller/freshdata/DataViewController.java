package com.ruoyi.web.controller.freshdata;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.common.core.controller.BaseController;

/**
 * 数据分析页面视图Controller
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/freshdata")
public class DataViewController extends BaseController {
    
    private String prefix = "freshdata";
    
    /**
     * 数据概览页面
     */
    @RequiresPermissions("freshdata:analysis:overview")
    @GetMapping("/overview")
    public String overview() {
        return prefix + "/overview";
    }
    
    /**
     * 用户行为分析页面
     */
    @RequiresPermissions("freshdata:analysis:login")
    @GetMapping("/behavior")
    public String behavior() {
        return prefix + "/behavior";
    }
    
    /**
     * 消费行为分析页面
     */
    @RequiresPermissions("freshdata:analysis:consumption")
    @GetMapping("/consumption")
    public String consumption() {
        return prefix + "/consumption";
    }
    
    /**
     * 用户画像分析页面
     */
    @RequiresPermissions("freshdata:analysis:profile")
    @GetMapping("/profile")
    public String profile() {
        return prefix + "/profile";
    }
    
    /**
     * 社交行为分析页面
     */
    @RequiresPermissions("freshdata:analysis:social")
    @GetMapping("/social")
    public String social() {
        return prefix + "/social";
    }
    
    /**
     * 内容偏好分析页面
     */
    @RequiresPermissions("freshdata:analysis:content")
    @GetMapping("/content")
    public String content() {
        return prefix + "/content";
    }
}

