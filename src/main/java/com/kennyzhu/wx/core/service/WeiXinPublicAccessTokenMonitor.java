package com.kennyzhu.wx.core.service;

import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Desc: 监控验证易信公众平台有效性
 * <p/>Date: 2014/11/20
 * <br/>Time: 11:49
 * <br/>User: ylzhu
 */
public class WeiXinPublicAccessTokenMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinPublicAccessTokenWrapperBak.class);

    /**
     * 验证Token有效性
     *
     * @return
     */
    protected boolean validAccessToken() {
        WeiXinPublicAccessToken accessToken = WeiXinPublicAccessTokenFactory.getInstance().getWeiXinPublicAccessToken();
        if (accessToken == null) {
            LOGGER.error("#Get WeiXin public accessToken from cache return null!");
            return false;
        }
        return WeiXinPublicAccessTokenFactory.getInstance().validAccessToken(accessToken);
    }

    /**
     * 心跳检测
     */
    public void monitor() {
        LOGGER.info("Begin to monitor！");
        try {
            if (!heartbeat()) {
                rebuildAccessToken();
            }
            LOGGER.info("WeiXin public access token monitor:end current time is " + new Date());
        } catch (Exception e) {
            LOGGER.error("WeiXin public access token monitor heartbeat error!Cause:", e);
        }
    }

    /**
     * 心跳检测
     */
    private boolean heartbeat() {
        try {
            if (validAccessToken()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                LOGGER.info("WeiXin public access token monitor: token is valid." + simpleDateFormat.format(new Date()));
                return true;
            } else {
                LOGGER.error("WeiXin public access token monitor: token is expired.Build it again.");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("WeiXin public access token monitor: return error!msg is :" + e.getMessage(), e);
        }
        return false;
    }

    protected void rebuildAccessToken() {
        WeiXinPublicAccessTokenFactory.getInstance().getAccessTokenFromWeiXin();
    }

}
