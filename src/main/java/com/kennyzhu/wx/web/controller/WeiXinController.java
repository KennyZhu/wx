package com.kennyzhu.wx.web.controller;

import com.kennyzhu.wx.core.common.BaseController;
import com.kennyzhu.wx.core.model.AccessToken;
import com.kennyzhu.wx.core.model.WeiXinPublicEventMsg;
import com.kennyzhu.wx.core.model.WeiXinPublicMsg;
import com.kennyzhu.wx.core.service.HttpService;
import com.kennyzhu.wx.core.service.WeiXinPublicConstant;
import com.kennyzhu.wx.core.service.WeiXinPublicSubEventHandler;
import com.kennyzhu.wx.core.service.WeiXinService;
import com.kennyzhu.wx.core.util.WeiXinUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ylzhu on 2016/7/7.
 */
@Controller
public class WeiXinController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private WeiXinService weiXinService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private WeiXinPublicSubEventHandler weiXinPublicSubEventHandler;

    /**
     * 接受信息
     *
     * @returnS
     */
    @RequestMapping(value = "/weixin/msg.html")
    public void msg(HttpServletResponse response, HttpServletRequest request, String echostr, String timestamp,
                    String nonce, String signature) {
        try {
//            LOGGER.info("##echostr=" + echostr + ",timestamp" + timestamp + ",nonce=" + nonce + ",signature" + signature);
            if (StringUtils.isNotBlank(echostr)) {
                serverVerify(response, echostr, timestamp, nonce, signature);
            } else {
                msgHandler(request, response);
            }
        } catch (Exception e) {
            LOGGER.error("Validate Token Error!", e);
        }
    }

    /*
    记录用户关注微信公众号时，用户openid
     */
//    private void msgHandler(HttpServletRequest request) throws Exception {
//        String xml = getXmlFromRequest(request);
//        WeiXinPublicMsg weiXinPublicMsg = WeiXinUtil.parseMsg(xml);
//        if (weiXinPublicMsg instanceof WeiXinPublicEventMsg) {
//            Map<String, Object> paramMap = new HashMap<>();
//            weiXinPublicSubEventHandler.handle(weiXinPublicMsg, paramMap);
//        }
//    }

    //响应微信服务端消息
    private void msgHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xml = getXmlFromRequest(request);
        WeiXinPublicMsg weiXinPublicMsg = WeiXinUtil.parseMsg(xml);
        if (weiXinPublicMsg == null) {
            LOGGER.info("#Xml is " + xml);
            return;
        }
        if (weiXinPublicMsg instanceof WeiXinPublicEventMsg) {
            Map<String, Object> paramMap = new HashMap<>();
            weiXinPublicSubEventHandler.handle(weiXinPublicMsg, paramMap);
        }
        String toUserName = weiXinPublicMsg.getToUserName();//公众账号名字
        String fromUserName = weiXinPublicMsg.getFromUserName();//用户一个openId
        long times = System.currentTimeMillis();
        String createTime = String.valueOf(times);
        String msgContent = msgContent();
        String eventType = weiXinPublicMsg.getEventType();//获取事件类型
        if (eventType.toLowerCase().equals("subscribe")) {
            //给你微信服务端发送消息
            StringBuffer str = new StringBuffer();
            str.append("<xml>");
            str.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");
            str.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");
            str.append("<CreateTime>" + createTime + "</CreateTime>");
            str.append("<MsgType><![CDATA[text]]></MsgType>");
            str.append("<Content><![CDATA[" + msgContent + "]]></Content>");
            str.append("</xml>");
            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "inline");
            PrintWriter writer = null;
            try {
                LOGGER.info("发送微信服务的消息内容: " + str.toString());
                writer = response.getWriter();
                writer.write(str.toString());
                LOGGER.info("发送微信服务的消息: success");
            } catch (Exception e) {
                LOGGER.error("Server verify error!Cause: ", e);
            }
        }
    }

//    //发送带有表情的文本信息，需要对表情做处理
//    //就是将代码表中的U+替换为0x
//    private String emoji(int hexEmoji) {
//        return String.valueOf(Character.toChars(hexEmoji));
//    }

    //发送给用户的消息
    private String msgContent() {
        String emoji_xin = "[爱心]", emoji_qin = "[亲亲]", emoji_gz = "[鼓掌]";//emoji(0x1F61A)
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎关注Letsmoney悦赚！恭喜你成为赚米一员！" + emoji_xin + emoji_xin);
        sb.append("\n\n无论你是帮自己，还是帮亲友，发布借款申请就有机会得到丰厚现金奖励！点击<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc96e42da7088e09f&redirect_uri=http%3A%2F%2Fdddtest.91caijia.com%2Fletsmoney%2Fhtml%2FSubmit.html&response_type=code&scope=snsapi_base#wechat_redirect\">【立即报料】</a>，提交借款信息！" + emoji_qin + emoji_qin);
        sb.append("\n\n转发<a href=\"http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000002&idx=1&sn=f243cc5efecf7f791acee411a90f7389&scene=18&scene=21#wechat_redirect\">【Let's money!】</a>，邀请朋友报料，你也有奖励！更多拿奖秘籍，熟读<a href=\"http://mp.weixin.qq.com/s?__biz=MzI0OTQ0MTc5Nw==&mid=100000011&idx=1&sn=95b813e28c05b43173b7e25fdb2e76d5&scene=18#wechat_redirect\">【拿奖攻略】~</a>" + emoji_gz + emoji_gz);
        sb.append("\n\nEverybody, let’s money! 悦来~ 悦赚~");
        return sb.toString();
    }

    /**
     * 校验使用
     */
    private void serverVerify(HttpServletResponse response, String echostr, String timestamp, String nonce,
                              String signature) {

            if (WeiXinUtil.validateSignature(WeiXinPublicConstant.VERIFY_TOKEN, timestamp, nonce, signature)) {
            response.setContentType("text/plain; charset=UTF-8");
            response.setHeader("Content-Disposition", "inline");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(echostr);
                LOGGER.info("#####WeiXin echoStr is " + echostr);
            } catch (Exception e) {
                LOGGER.error("Server verify error!Cause: ", e);
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        } else {
            LOGGER.error("Validate Token Fail!");
        }
    }

    /**
     * 获取消息报文数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    private String getXmlFromRequest(HttpServletRequest request) throws Exception {
        BufferedReader br = null;
        String line = null;
        StringBuilder sb = new StringBuilder();
        br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 获取用户OpenId
     *
     * @param code
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixin/getUserToken")
    public String getUserAccessToken(String code, HttpServletResponse response) {
        if (StringUtils.isBlank(code)) {
            return buildInvalidParamJson();
        }
        AccessToken accessToken =
                weiXinService.getUserAccessToken(code);
        try {
            response.sendRedirect("");//跳转到首页
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取微信Code
     *
     * @param redirectUrl
     * @return
     */
    @RequestMapping(value = "/weixin/getCode")
    public String getCode(String redirectUrl, HttpServletResponse response) {
        try {
            LOGGER.info("#RedirectUrl is " + redirectUrl);
            redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2811ef8f37a5952b&redirect_uri=" + redirectUrl + "&response_type=code&scope=snsapi_base#wechat_redirect";
            response.sendRedirect(redirectUrl);
            LOGGER.info("#After send Redirect.");
        } catch (Exception e) {
            LOGGER.error("#Get code error.Cause:", e);
        }
        return null;
    }
}
