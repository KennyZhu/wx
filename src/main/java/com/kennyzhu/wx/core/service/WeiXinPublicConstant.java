package com.kennyzhu.wx.core.service;

import org.apache.commons.lang3.StringUtils;

/**
 * User: KennyZhu
 * Date: 16/6/25
 * Desc:
 */
public final class WeiXinPublicConstant {
    public static final String APP_ID = "wxc96e42da7088e09f";
    public static final String APP_SECRET = "d05e0be6537a7e12ec463267ae842bab";
    public static final String VERIFY_TOKEN = "c0c63192d9887335a250927db83eb2b3";

    public static final String PUBLIC_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?&";

    public static final String USER_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?";

    public static final String GET_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";

    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

    /**
     * @param redirectUri
     * @param param
     * @return
     */
    public static String getCodeUrl(String redirectUri, String param) {
        return GET_CODE_URL + "appid=" + APP_ID + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_userinfo&state=" + param + "#wechat_redirect";
    }

    /**
     * 获取用户OpenId
     *
     * @param code
     * @return
     */
    public static String getUserOpenIdUrl(String code) {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
    }

    public static String getWeiXinGetHeartBeatMonitorUrl(String accessToken) {
        //TODO
        return "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=" + accessToken;
    }

    /**
     * 获取公众号Token
     *
     * @return
     */
    public static String getPublicAccessTokenUrl() {
        return PUBLIC_ACCESS_TOKEN_URL + "appid=" + APP_ID + "&secret=" + APP_SECRET + "&grant_type=client_credential";
    }

    /**
     * 获取用户Token
     *
     * @return
     */
    public static String getUserAccessTokenUrl(String code) {
        return USER_ACCESS_TOKEN_URL + "appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
    }

    /**
     * @param accessToken
     * @param openId
     * @return
     */
    public static String getUserInfoUrl(String accessToken, String openId) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
            return null;
        }
        return USER_INFO_URL + "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";

    }

    public static String getPublicCreateMenuUrl(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        return CREATE_MENU_URL + accessToken;
    }

    public static String getDeleteCreateMenuUrl(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        return DELETE_MENU_URL + accessToken;
    }
}
