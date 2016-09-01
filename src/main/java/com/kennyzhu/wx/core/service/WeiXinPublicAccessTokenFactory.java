package com.kennyzhu.wx.core.service;


import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;
import com.kennyzhu.wx.core.util.JsonUtil;
import com.kennyzhu.wx.core.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Desc: 微信公众平台 AccessToken Factory
 * <br/>User: ylzhu
 */
public final class WeiXinPublicAccessTokenFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WeiXinPublicAccessTokenFactory.class);
    private static final WeiXinPublicAccessTokenFactory INSTANCE = new WeiXinPublicAccessTokenFactory();

    private WeiXinPublicAccessTokenFactory() {
    }

    public static WeiXinPublicAccessTokenFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 获取微信公众平台AccessToken
     *
     * @return see {@link WeiXinPublicAccessToken}
     */
    public WeiXinPublicAccessToken getWeiXinPublicAccessToken() {
        try {
            WeiXinPublicAccessToken accessToken = WeiXinPublicAccessTokenCache.getInstance().get(WeiXinPublicConstant.APP_ID);
            if (accessToken != null) {
                LOG.info("########Get WeiXinAccessToken from Cache!");
                return accessToken;
            }
            LOG.info("#Get accesstoken from cache return null.Begin to build.");
            accessToken = getAccessTokenFromWeiXin();
            return accessToken;
        } catch (Exception e) {
            LOG.error("Get WeiXin Public AccessToken error!Cause:", e);
        }
        return null;
    }

    /**
     * 直接从微信获取Token，微信公众平台有限制，每天200次，慎用
     *
     * @return
     */
    public WeiXinPublicAccessToken getAccessTokenFromWeiXin() {
        try {
            WeiXinPublicAccessToken accessToken = WeiXinPublicAccessTokenWrapper.getInstance()
                    .buildWeiXinPublicAccessToken().getPublicAccessToken();

            WeiXinPublicAccessTokenCache.getInstance().set(WeiXinPublicConstant.APP_ID, accessToken);
            return accessToken;
        } catch (Exception e) {
            LOG.error("Add WeiXin public access token to cache error!Cause:", e);
        }
        return null;
    }


    /**
     * 验证WeiXin Public AccessToken 有效性
     *
     * @param accessToken
     * @return
     */
    public boolean validAccessToken(WeiXinPublicAccessToken accessToken) {
        if (accessToken != null && StringUtils.isNotBlank(accessToken.getAccess_token())) {
            String getPaInfoUrl = WeiXinPublicConstant.getWeiXinGetHeartBeatMonitorUrl(accessToken.getAccess_token());
            try {
                HttpService HTTPSERVICE = SpringContextHolder.getBean("httpServiceImpl");
                String paInfoResult = HTTPSERVICE.sendGetRequest(getPaInfoUrl);
                LOG.info("##########Get paInfoResult url is " + getPaInfoUrl + " return " + paInfoResult);
                if (StringUtils.isNotBlank(paInfoResult)) {
                    Map<String, Object> paInfoResultMap = JsonUtil.jsonToMap(paInfoResult);
                    Integer errorCode = (Integer) paInfoResultMap.get("errcode");
                    if (errorCode != null) {
                        LOG.error("WeiXin public access token monitor: yixin return error,please check immediately! Msg is "
                                + paInfoResult);

                        return false;
                    }
                    LOG.info("WeiXin public access token monitor: valid token return true.");
                    return true;
                }
            } catch (Exception e) {
                LOG.error("Valid WeiXin access token error!Cause:", e);
            }
        }
        return false;
    }
}
