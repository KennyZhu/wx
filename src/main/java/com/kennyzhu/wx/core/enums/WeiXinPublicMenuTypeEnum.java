package com.kennyzhu.wx.core.enums;

/**
 * Desc:微信公众平台自定义菜单支持的类型
 * <br/>User: ylzhu
 */
public enum WeiXinPublicMenuTypeEnum {
    CLICK("click"), VIEW("view"), VIEW_LIMITED("view_limited");

    private String type;

    private WeiXinPublicMenuTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
