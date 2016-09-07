package com.kennyzhu.wx.web.controller;

import com.kennyzhu.wx.core.common.BaseController;
import com.kennyzhu.wx.core.model.AccessToken;
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
            if (StringUtils.isNotBlank(echostr)) {
                serverVerify(response, echostr, timestamp, nonce, signature);
            } else {
//                msgHandler(request, response);
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

//

//    //发送带有表情的文本信息，需要对表情做处理
//    //就是将代码表中的U+替换为0x
//    private String emoji(int hexEmoji) {
//        return String.valueOf(Character.toChars(hexEmoji));
//    }

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
