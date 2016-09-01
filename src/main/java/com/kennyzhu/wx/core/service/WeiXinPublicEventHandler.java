package com.kennyzhu.wx.core.service;


import com.kennyzhu.wx.core.model.WeiXinPublicMsg;

import java.util.Map;

/**
 * Desc:
 * <p/>Date: 2014/11/27
 * <br/>Time: 11:00
 * <br/>User: ylzhu
 */
public interface WeiXinPublicEventHandler {
    public void handle(WeiXinPublicMsg msg, Map<String, Object> params);
}
