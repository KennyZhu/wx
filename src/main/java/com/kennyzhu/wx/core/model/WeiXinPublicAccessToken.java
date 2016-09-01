package com.kennyzhu.wx.core.model;

import com.kennyzhu.wx.core.exception.BusinessException;
import com.kennyzhu.wx.core.service.HttpService;
import com.kennyzhu.wx.core.service.WeiXinPublicConstant;
import com.kennyzhu.wx.core.util.JsonUtil;
import com.kennyzhu.wx.core.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class WeiXinPublicAccessToken {
    private static final long serialVersionUID = -8856906072461177637L;
    private String access_token;
    private int expires_in;//有效时长，单位：秒

    private static final Log LOG = LogFactory.getLog(WeiXinPublicAccessToken.class);

    /**
     * 获取AccessToken
     *
     * @return
     */
    public WeiXinPublicAccessToken buildAccessToken() throws BusinessException {
        LOG.info("Build WeiXin access token begin.");
        String accessTokenUrl = WeiXinPublicConstant.getPublicAccessTokenUrl();
        HttpService httpService = SpringContextHolder.getBean("httpServiceImpl");
        try {
            String accessTokenResult = httpService.sendGetRequest(accessTokenUrl);
            LOG.info("Build WeiXin access token return : " + accessTokenResult);
            if (StringUtils.isNotBlank(accessTokenResult)) {
                Map<String, Object> accessTokenMap = JsonUtil.jsonToMap(accessTokenResult);
                Integer errorCode = (Integer) accessTokenMap.get("errcode");
                if (errorCode != null) {
                    LOG.fatal("Build WeiXin access token error!WeiXin return : " + accessTokenResult);
                    throw new BusinessException("Build WeiXin access token error!WeiXin return : " + accessTokenResult);
                } else {
                    this.access_token = (String) accessTokenMap.get("access_token");
                    this.expires_in = (Integer) accessTokenMap.get("expires_in");
                }
            }
        } catch (Exception e) {
            LOG.fatal("Build WeiXin access token error!Cause:{}", e);
            throw e;
        }
        return this;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

}
