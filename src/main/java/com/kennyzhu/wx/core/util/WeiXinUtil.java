package com.kennyzhu.wx.core.util;

import com.kennyzhu.wx.core.enums.WeiXinPublicEventTypeEnum;
import com.kennyzhu.wx.core.enums.WeiXinPublicMsgTypeEnum;
import com.kennyzhu.wx.core.model.WeiXinPublicEventMsg;
import com.kennyzhu.wx.core.model.WeiXinPublicMsg;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * User: KennyZhu
 * Date: 16/6/20
 * Desc:
 */
public final class WeiXinUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinUtil.class);

    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 第三方公众平台验证算法，易信、微信使用同一套算法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String genSignatureForVerify(String token, String timestamp, String nonce) {
        String[] sourceStrs = new String[]
                {token, timestamp, nonce};
        Arrays.sort(sourceStrs);
        StringBuffer str = new StringBuffer();
        for (String source : sourceStrs) {
            str.append(source);
        }
        String strDigest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(str.toString().getBytes());
            strDigest = bufferToHex(digest);
        } catch (NoSuchAlgorithmException nsae) {
            LOGGER.error("Gen Third Public Signature error！Cause：", nsae);
        }
        return strDigest;
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX_DIGITS[(bt & 0xf0) >>> 4];// 取字节中高 4 位的数字转换
        char c1 = HEX_DIGITS[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 加密/校验流程如下：
     * 1. 将token、timestamp、nonce三个参数进行字典序排序
     * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
     * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于易信
     *
     * @return
     */
    public static boolean validateSignature(String token, String timestamp, String nonce, String signature) {
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce) || StringUtils.isBlank(signature)) {
            LOGGER.error("Invalid param!");
            return false;
        }
        String sign = genSignatureForVerify(token, timestamp, nonce);
        return signature.equals(sign);
    }

    public static WeiXinPublicMsg parseMsg(String xml) {
        try {
            LOGGER.info("#WeiXin msg is " + xml);
            StringReader reader = new StringReader(xml);
            SAXBuilder sb = new SAXBuilder();

            Document doc = sb.build(reader);
            Element root = doc.getRootElement();
            String msgType = root.getChildText("MsgType");
            String toUserName = root.getChildText("ToUserName");
            String fromUserName = root.getChildText("FromUserName");
            String createTime = root.getChildText("CreateTime");

            WeiXinPublicMsgTypeEnum msgTypeEnum = WeiXinPublicMsgTypeEnum.getByMsgType(msgType);
            if (WeiXinPublicMsgTypeEnum.EVENT == msgTypeEnum) {
                String eventType = root.getChildText("Event");
                if (WeiXinPublicEventTypeEnum.SUBSCRIBE == WeiXinPublicEventTypeEnum.getEvent(eventType)) {
                    WeiXinPublicEventMsg eventMsg = new WeiXinPublicEventMsg();
                    eventMsg.setToUserName(toUserName);
                    eventMsg.setFromUserName(fromUserName);
                    eventMsg.setCreateTime(createTime);
                    eventMsg.setEvent(eventType);
                    eventMsg.setEventType(eventType);
                    return eventMsg;
                }
            }
        } catch (Exception e) {
            LOGGER.error("#Parse WeiXinPublic Msg error!Cause:", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("#" + URLEncoder.encode("http://dddtest.91caijia.com/letsmoney/loan/queryBase.html"));
        System.out.println(MD5Util.get("LetsMoney+1", "UTF-8"));
    }


}
