package com.kennyzhu.wx.core.service.impl;

import com.kennyzhu.wx.core.enums.WeiXinPublicMenuTypeEnum;
import com.kennyzhu.wx.core.model.AccessToken;
import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;
import com.kennyzhu.wx.core.model.WeiXinPublicMenu;
import com.kennyzhu.wx.core.service.*;
import com.kennyzhu.wx.core.util.JsonUtil;
import com.kennyzhu.wx.core.util.WeiXinPublicUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ylzhu on 2016/7/7.
 */
@Service
public class WeiXinServiceImpl implements WeiXinService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinServiceImpl.class);

    @Autowired
    private HttpService httpService;

    @Override
    public AccessToken getUserAccessToken(String code) {
        try {
            String result = httpService.sendGetRequest(WeiXinPublicConstant.getUserAccessTokenUrl(code));
            LOGGER.info("#Get accessToken by code:" + code + " return :" + result);
            return (AccessToken) JsonUtil.getBeanByJson(result, AccessToken.class);
        } catch (Exception e) {
            LOGGER.error("#Get accessToken error.Cause:", e);
        }
        return null;
    }

    @Override
    public WeiXinPublicAccessToken getPublicAccessToken() {
        try {
            String result = httpService.sendGetRequest(WeiXinPublicConstant.getPublicAccessTokenUrl());
            return (WeiXinPublicAccessToken) JsonUtil.getBeanByJson(result, WeiXinPublicAccessToken.class);
        } catch (Exception e) {
            LOGGER.error("#Get accessToken error.Cause:", e);
        }
        return null;
    }

    @Override
    public WeiXinUserInfo getWeiXinUserInfo(String openId) {
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        try {
            WeiXinPublicAccessToken token = WeiXinPublicAccessTokenFactory.getInstance().getWeiXinPublicAccessToken();
            if (token == null) {
                LOGGER.error("#Get accessToken return null.");
            }
            String result = httpService.sendGetRequest(WeiXinPublicConstant.getUserInfoUrl(token.getAccess_token(), openId));
            LOGGER.info("#Get accessToken return " + result);
            return (WeiXinUserInfo) JsonUtil.getBeanByJson(result, WeiXinUserInfo.class);
        } catch (Exception e) {
            LOGGER.error("#Get accessToken error.Cause:", e);
        }
        return null;
    }


    @Override
    public String sendTextMsgToUser(String openId, String content) {
        /**
         if (StringUtils.isBlank(openId) || StringUtils.isBlank(content))
         {
         LOGGER.error("#Send text msg to user Invalid param!");
         return null;
         }
         YiXinPublicAccessToken yiXinPublicAccessToken = YiXinPublicAccessTokenFactory.getInstance()
         .getYiXinPublicAccessToken();
         if (yiXinPublicAccessToken == null)
         {
         LOGGER.error("#Get YiXin public accessToken return null!");
         return null;
         }
         Map<String, String> contentMap = new HashMap<>();
         contentMap.put("content", content);

         Map<String, Object> dataMap = new HashMap<>();
         dataMap.put("touser", openId);
         dataMap.put("msgtype", "text");
         dataMap.put("text", contentMap);
         String sourceJson = JsonUtil.mapToJson(dataMap);
         String url = ThirdPublicConstant.getYiXinPublicSendMsgUrl(yiXinPublicAccessToken.getAccess_token());
         String result = null;
         try
         {
         result = httpService.sendHttpsPostRequest(url, sourceJson, "UTF-8");
         }
         catch (Exception e)
         {
         LOGGER.error("#Send text msg to user error!Cause:", e);
         }
         return result;**/
        return null;
    }


    private String buildWeiXinPublicMenus() {
        WeiXinPublicMenu yjbl = new WeiXinPublicMenu("有奖报料");
        WeiXinPublicMenu addLoanMenu = new WeiXinPublicMenu("立即报料");
        addLoanMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        addLoanMenu.buildUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinPublicConstant.APP_ID + "&redirect_uri=http%3A%2F%2Fdddtest.91caijia.com%2Fletsmoney%2Fhtml%2FSubmit.html&response_type=code&scope=snsapi_base#wechat_redirect");

        WeiXinPublicMenu loanQueryMenu = new WeiXinPublicMenu("进度查询");
        loanQueryMenu.buildUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeiXinPublicConstant.APP_ID + "&redirect_uri=http%3A%2F%2Fdddtest.91caijia.com%2Fletsmoney%2Floan%2FqueryBase.html&response_type=code&scope=snsapi_base#wechat_redirect");
//        loanQueryMenu.buildUrl("http://dddtest.91caijia.com/letsmoney/html/qureBase2.html");
        loanQueryMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);

        WeiXinPublicMenu blMenu = new WeiXinPublicMenu("报料那些事儿");
        blMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        blMenu.buildUrl("http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000004&idx=1&sn=db2afe7184ede13ee3dc467754bc00a2&scene=18#wechat_redirect");

        WeiXinPublicMenu jkMenu = new WeiXinPublicMenu("借款那些事儿");
        jkMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        jkMenu.buildUrl("http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000006&idx=1&sn=860010f24b06dbbcee9a3a7c8074b07f&scene=18#wechat_redirect");

        yjbl.buildSubMenu(Arrays.asList(addLoanMenu, loanQueryMenu, blMenu, jkMenu));


        WeiXinPublicMenu ylyz = new WeiXinPublicMenu("悦来悦赚");

        WeiXinPublicMenu letsMoney = new WeiXinPublicMenu("Let's money");
        letsMoney.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        letsMoney.buildUrl("http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000002&idx=1&sn=f243cc5efecf7f791acee411a90f7389&scene=18&scene=21#wechat_redirect");

        WeiXinPublicMenu najiangMenu = new WeiXinPublicMenu("拿奖攻略");
        najiangMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
//        najiangMenu.buildUrl("http://dddtest.91caijia.com/letsmoney/html/拿奖攻略.html");
        najiangMenu.buildUrl("http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000011&idx=1&sn=95b813e28c05b43173b7e25fdb2e76d5&scene=18#wechat_redirect");


        ylyz.buildSubMenu(Arrays.asList(letsMoney, najiangMenu));


        WeiXinPublicMenu help = new WeiXinPublicMenu("帮助锦囊");
        WeiXinPublicMenu tucaoMenu = new WeiXinPublicMenu("我要吐槽");
        tucaoMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        tucaoMenu.buildUrl("https://jinshuju.net/f/Doq1jA");

        WeiXinPublicMenu lianxiMenu = new WeiXinPublicMenu("联系我们");
        lianxiMenu.buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        lianxiMenu.buildUrl("http://dddtest.91caijia.com/letsmoney/html/联系我们.html");


        help.buildSubMenu(Arrays.asList(tucaoMenu, lianxiMenu));


        Map<String, List<Map<String, Object>>> buttons = new HashMap<>();

        List<Map<String, Object>> menus = new ArrayList<>();
        menus.add(WeiXinPublicUtil.convertMenuToMap(ylyz));
        menus.add(WeiXinPublicUtil.convertMenuToMap(yjbl));
        menus.add(WeiXinPublicUtil.convertMenuToMap(help));
        buttons.put("button", menus);

        String result = JsonUtil.getJsonString(buttons);
        LOGGER.info("#WeiXin Pub Menu is " + result);
        return result;
    }


    @Override
    public boolean deleteWeiXinPublicMenu(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            LOGGER.error("#Invalid AccessToken:accessToken is empty!");
            return false;
        }
        String url = WeiXinPublicConstant.getDeleteCreateMenuUrl(accessToken);
        String resultJson = null;

        try {
            resultJson = httpService.sendGetRequest(url);
            Map<String, Object> resultMap = JsonUtil.jsonToMap(resultJson);
            String errcode = String.valueOf(resultMap.get("errcode"));
            if ("0".equals(errcode)) {
                return true;
            } else {
                LOGGER.error("#Delete weixin public menu return error!Return is :" + resultJson + " errcode is " + errcode);
            }
        } catch (Exception e) {
            LOGGER.error("#Delete weixin public menu return error return Json is " + resultJson + "!Cause:", e);
        }

        return false;
    }

    @Override
    public boolean createWeiXinPublicMenu(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            LOGGER.error("#Invalid AccessToken");
            return false;
        }
        String url = WeiXinPublicConstant.getPublicCreateMenuUrl(accessToken);
        String resultJson = null;

        String content = buildWeiXinPublicMenus();
        LOGGER.info("#Create WeiXin public menu url is " + url + "  content is " + content);
        try {
            resultJson = httpService.sendPostRequest(url, content);
            Map<String, Object> resultMap = JsonUtil.jsonToMap(resultJson);
            String errcode = String.valueOf(resultMap.get("errcode"));
            if ("0".equals(errcode)) {
                return true;
            } else {
                LOGGER.error("#Create weixin public menu return error!Return is :" + resultJson + " errcode is " + errcode);
            }
        } catch (Exception e) {
            LOGGER.error("#Create weixin public menu return error return Json is " + resultJson + "!Cause:", e);
        }

        return false;
    }

    public static void main(String[] args) {
        LOGGER.info("#" + URLEncoder.encode("http://dddtest.91caijia.com/letsmoney/loan/queryBase.html"));
    }
}
