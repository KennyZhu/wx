package com.kennyzhu.wx.core.service;

import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;
import com.kennyzhu.wx.core.util.JsonUtil;
import com.kennyzhu.wx.core.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Desc:微信公众平台AccessToken 封装类
 * <p/>Date: 2014/10/28
 * <br/>Time: 17:01
 * <br/>User: ylzhu
 */
public class WeiXinPublicAccessTokenWrapperBak implements Serializable {
    private WeiXinPublicAccessToken publicAccessToken;

    private static final WeiXinPublicAccessTokenWrapperBak INSTANCE = new WeiXinPublicAccessTokenWrapperBak();
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinPublicAccessTokenWrapperBak.class);

    private WeiXinPublicAccessTokenWrapperBak() {
    }

    public static WeiXinPublicAccessTokenWrapperBak getInstance() {
        return INSTANCE;
    }

    /**
     * 获取AccessToken
     *
     * @return
     */
    public WeiXinPublicAccessTokenWrapperBak buildWeiXinPublicAccessToken() {
        try {
            LOGGER.info("#WeiXin Build New WeiXinPublicAccessToken!");
            publicAccessToken = buildAccessToken();
        } catch (Exception e) {
            LOGGER.error(" Build WeiXinPublicAccessToken error!Cause:", e);
            throw e;
        }
        return this;
    }

    private WeiXinPublicAccessToken buildAccessToken() {
        try {

            HttpService httpService = SpringContextHolder.getBean("httpServiceImpl");
            String result = httpService.sendGetRequest(WeiXinPublicConstant.getPublicAccessTokenUrl());
            return (WeiXinPublicAccessToken) JsonUtil.getBeanByJson(result, WeiXinPublicAccessToken.class);
        } catch (Exception e) {
            LOGGER.error("#Get accessToken error.Cause:", e);
        }
        return null;
    }

    public WeiXinPublicAccessToken getPublicAccessToken() {
        return publicAccessToken;
    }

}
