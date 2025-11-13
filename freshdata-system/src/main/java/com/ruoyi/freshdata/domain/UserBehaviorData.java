package com.ruoyi.freshdata.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 生鲜电商用户行为数据
 * 
 * @author ruoyi
 */
public class UserBehaviorData extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 首次下单时间 */
    @Excel(name = "首次下单时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstOrderTime;

    /** 首次下单金额 */
    @Excel(name = "首次下单金额")
    private Double firstOrderPrice;

    /** 账户年龄(月) */
    @Excel(name = "账户年龄(月)")
    private Double ageMonth;

    /** 城市编号 */
    @Excel(name = "城市编号")
    private String cityNum;

    /** 平台编号 */
    @Excel(name = "平台编号")
    private Double platformNum;

    /** 设备型号编号 */
    @Excel(name = "设备型号")
    private Double modelNum;

    /** 登录天数 */
    @Excel(name = "登录天数")
    private Double loginDay;

    /** 登录时间差 */
    @Excel(name = "登录时间差")
    private Double loginDiffTime;

    /** 距离天数 */
    @Excel(name = "距离天数")
    private Double distanceDay;

    /** 登录时长 */
    @Excel(name = "登录时长")
    private Double loginTime;

    /** 启动时长 */
    @Excel(name = "启动时长")
    private Double launchTime;

    /** 订阅数 */
    @Excel(name = "订阅数")
    private Double subscribeNum1;

    /** 添加好友 */
    @Excel(name = "添加好友")
    private Double addFriend;

    /** 加入群组 */
    @Excel(name = "加入群组")
    private Double addGroup;

    /** 访问次数 */
    @Excel(name = "访问次数")
    private Double visitNum;

    /** 搜索次数 */
    @Excel(name = "搜索次数")
    private Double searchNum;

    /** 完成次数 */
    @Excel(name = "完成次数")
    private Double finishNum;

    /** 详情页访问 */
    @Excel(name = "详情页访问")
    private Double detailNum;

    /** 优惠券数量 */
    @Excel(name = "优惠券数量")
    private Double couponNum;

    /** 主页访问 */
    @Excel(name = "主页访问")
    private Double mainHome;

    /** 主页访问2 */
    @Excel(name = "主页访问2")
    private Double mainHome2;

    /** 主页面 */
    @Excel(name = "主页面")
    private Double mainPage;

    /** 购物车页面 */
    @Excel(name = "购物车页面")
    private Double shopCartPage;

    /** 主界面 */
    @Excel(name = "主界面")
    private Double mainMime;

    /** 收藏标签 */
    @Excel(name = "收藏标签")
    private Double collectionTab;

    /** 推荐访问 */
    @Excel(name = "推荐访问")
    private Double recommendVisit;

    /** 优惠券访问 */
    @Excel(name = "优惠券访问")
    private Double couponVisit;

    /** 购物点击购买 */
    @Excel(name = "购物点击购买")
    private Double shopClickBuy;

    /** 任务 */
    @Excel(name = "任务")
    private Double task;

    /** 内容阅读 */
    @Excel(name = "内容阅读")
    private Double contentRead;

    /** 内容收藏 */
    @Excel(name = "内容收藏")
    private Double contentCollect;

    /** 销售标签 */
    @Excel(name = "销售标签")
    private Double saleTab;

    /** 红包点击 */
    @Excel(name = "红包点击")
    private Double redClick;

    /** 分享 */
    @Excel(name = "分享")
    private Double share;

    /** 点击对话框 */
    @Excel(name = "点击对话框")
    private Double clickDialog;

    /** 用户活跃度(0-不活跃, 1-活跃) */
    @Excel(name = "用户活跃度")
    private Integer result;

    // Getter and Setter methods

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getFirstOrderTime() {
        return firstOrderTime;
    }

    public void setFirstOrderTime(Date firstOrderTime) {
        this.firstOrderTime = firstOrderTime;
    }

    public Double getFirstOrderPrice() {
        return firstOrderPrice;
    }

    public void setFirstOrderPrice(Double firstOrderPrice) {
        this.firstOrderPrice = firstOrderPrice;
    }

    public Double getAgeMonth() {
        return ageMonth;
    }

    public void setAgeMonth(Double ageMonth) {
        this.ageMonth = ageMonth;
    }

    public String getCityNum() {
        return cityNum;
    }

    public void setCityNum(String cityNum) {
        this.cityNum = cityNum;
    }

    public Double getPlatformNum() {
        return platformNum;
    }

    public void setPlatformNum(Double platformNum) {
        this.platformNum = platformNum;
    }

    public Double getModelNum() {
        return modelNum;
    }

    public void setModelNum(Double modelNum) {
        this.modelNum = modelNum;
    }

    public Double getLoginDay() {
        return loginDay;
    }

    public void setLoginDay(Double loginDay) {
        this.loginDay = loginDay;
    }

    public Double getLoginDiffTime() {
        return loginDiffTime;
    }

    public void setLoginDiffTime(Double loginDiffTime) {
        this.loginDiffTime = loginDiffTime;
    }

    public Double getDistanceDay() {
        return distanceDay;
    }

    public void setDistanceDay(Double distanceDay) {
        this.distanceDay = distanceDay;
    }

    public Double getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Double loginTime) {
        this.loginTime = loginTime;
    }

    public Double getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Double launchTime) {
        this.launchTime = launchTime;
    }

    public Double getSubscribeNum1() {
        return subscribeNum1;
    }

    public void setSubscribeNum1(Double subscribeNum1) {
        this.subscribeNum1 = subscribeNum1;
    }

    public Double getAddFriend() {
        return addFriend;
    }

    public void setAddFriend(Double addFriend) {
        this.addFriend = addFriend;
    }

    public Double getAddGroup() {
        return addGroup;
    }

    public void setAddGroup(Double addGroup) {
        this.addGroup = addGroup;
    }

    public Double getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Double visitNum) {
        this.visitNum = visitNum;
    }

    public Double getSearchNum() {
        return searchNum;
    }

    public void setSearchNum(Double searchNum) {
        this.searchNum = searchNum;
    }

    public Double getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Double finishNum) {
        this.finishNum = finishNum;
    }

    public Double getDetailNum() {
        return detailNum;
    }

    public void setDetailNum(Double detailNum) {
        this.detailNum = detailNum;
    }

    public Double getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Double couponNum) {
        this.couponNum = couponNum;
    }

    public Double getMainHome() {
        return mainHome;
    }

    public void setMainHome(Double mainHome) {
        this.mainHome = mainHome;
    }

    public Double getMainHome2() {
        return mainHome2;
    }

    public void setMainHome2(Double mainHome2) {
        this.mainHome2 = mainHome2;
    }

    public Double getMainPage() {
        return mainPage;
    }

    public void setMainPage(Double mainPage) {
        this.mainPage = mainPage;
    }

    public Double getShopCartPage() {
        return shopCartPage;
    }

    public void setShopCartPage(Double shopCartPage) {
        this.shopCartPage = shopCartPage;
    }

    public Double getMainMime() {
        return mainMime;
    }

    public void setMainMime(Double mainMime) {
        this.mainMime = mainMime;
    }

    public Double getCollectionTab() {
        return collectionTab;
    }

    public void setCollectionTab(Double collectionTab) {
        this.collectionTab = collectionTab;
    }

    public Double getRecommendVisit() {
        return recommendVisit;
    }

    public void setRecommendVisit(Double recommendVisit) {
        this.recommendVisit = recommendVisit;
    }

    public Double getCouponVisit() {
        return couponVisit;
    }

    public void setCouponVisit(Double couponVisit) {
        this.couponVisit = couponVisit;
    }

    public Double getShopClickBuy() {
        return shopClickBuy;
    }

    public void setShopClickBuy(Double shopClickBuy) {
        this.shopClickBuy = shopClickBuy;
    }

    public Double getTask() {
        return task;
    }

    public void setTask(Double task) {
        this.task = task;
    }

    public Double getContentRead() {
        return contentRead;
    }

    public void setContentRead(Double contentRead) {
        this.contentRead = contentRead;
    }

    public Double getContentCollect() {
        return contentCollect;
    }

    public void setContentCollect(Double contentCollect) {
        this.contentCollect = contentCollect;
    }

    public Double getSaleTab() {
        return saleTab;
    }

    public void setSaleTab(Double saleTab) {
        this.saleTab = saleTab;
    }

    public Double getRedClick() {
        return redClick;
    }

    public void setRedClick(Double redClick) {
        this.redClick = redClick;
    }

    public Double getShare() {
        return share;
    }

    public void setShare(Double share) {
        this.share = share;
    }

    public Double getClickDialog() {
        return clickDialog;
    }

    public void setClickDialog(Double clickDialog) {
        this.clickDialog = clickDialog;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserBehaviorData{" +
                "userId='" + userId + '\'' +
                ", firstOrderTime=" + firstOrderTime +
                ", firstOrderPrice=" + firstOrderPrice +
                ", ageMonth=" + ageMonth +
                ", loginDay=" + loginDay +
                ", result=" + result +
                '}';
    }
}






