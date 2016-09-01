package com.kennyzhu.wx.core.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Desc:微信公众平台消息类型
 * <p/>Date: 2014/11/26
 * <br/>Time: 16:33
 * <br/>User: ylzhu
 */
public enum WeiXinPublicMsgTypeEnum {
    /**
     * 文本消息
     */
    TEXT("text"),
    /**
     * 图片消息
     */
    IMAGE("image"),
    /**
     * 音频消息
     */
    AUDIO("audio"),
    /**
     * 地理位置消息
     */
    LOCATION("location"),
    /**
     * 事件消息
     */
    EVENT("event");
    private String value;

    private WeiXinPublicMsgTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static WeiXinPublicMsgTypeEnum getByMsgType(String msgType) {
        if (StringUtils.isNotBlank(msgType)) {
            for (WeiXinPublicMsgTypeEnum enums : WeiXinPublicMsgTypeEnum.values()) {
                if (enums.getValue().equals(msgType)) {
                    return enums;
                }

            }
        }
        return null;
    }
}
