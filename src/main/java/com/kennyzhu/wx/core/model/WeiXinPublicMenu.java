package com.kennyzhu.wx.core.model;

import com.kennyzhu.wx.core.enums.WeiXinPublicMenuTypeEnum;

import java.util.List;

/**
 * /**
 * Desc:微信公众平台自定义菜单Model
 * <p/>Date: 2015/3/30
 * <br/>Time: 17:59
 * <br/>User: ylzhu
 */
public class WeiXinPublicMenu {
    private String name;
    /**
     * Required if menuType is {@link WeiXinPublicMenuTypeEnum#CLICK}
     */
    private String key;
    /**
     * Required if menuType is {@link WeiXinPublicMenuTypeEnum#VIEW}
     */
    private String url;

    /**
     * Required if menuType is {@link WeiXinPublicMenuTypeEnum#VIEW_LIMITED}
     */
    private String media_id;
    /**
     * see {@link WeiXinPublicMenuTypeEnum}
     */
    private WeiXinPublicMenuTypeEnum menuType;

    List<WeiXinPublicMenu> subMenus;

    public WeiXinPublicMenu(String name) {
        this.name = name;
    }

    public WeiXinPublicMenu buildMenuType(WeiXinPublicMenuTypeEnum menuType) {
        this.menuType = menuType;
        return this;
    }

    public WeiXinPublicMenu buildUrl(String url) {
        this.url = url;
        return this;
    }

    public WeiXinPublicMenu buildSubMenu(List<WeiXinPublicMenu> subMenus) {
        this.subMenus = subMenus;
        return this;
    }

    public WeiXinPublicMenu buildMediaId(String media_id) {
        this.media_id = media_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WeiXinPublicMenuTypeEnum getMenuType() {
        return menuType;
    }

    public void setMenuType(WeiXinPublicMenuTypeEnum menuType) {
        this.menuType = menuType;
    }

    public List<WeiXinPublicMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<WeiXinPublicMenu> subMenus) {
        this.subMenus = subMenus;
    }


    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Override
    public String toString() {
        return "WeiXinPublicMenu{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                ", media_id='" + media_id + '\'' +
                ", menuType=" + menuType +
                ", subMenus=" + subMenus +
                '}';
    }
}
