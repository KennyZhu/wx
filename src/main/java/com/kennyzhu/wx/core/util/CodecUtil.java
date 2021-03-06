package com.kennyzhu.wx.core.util;

/**
 * Desc:
 * <p/>Date: 2015-11-05
 * <br/>Time: 17:32
 * <br/>User: ylzhu
 */

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码、解码工具类，主要包括以下编解码方式：<br/>
 * hex、base36、base64、url、html、xml等<br />
 *
 * @author 开发支持中心。
 */
public final class CodecUtil {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    //用于Base36
    private static final char[] ALPHABET = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};
    static final int[] INVERTED_ALPHABET;
    //用于Base36。长度为68.
    static final String INIT_STR = "00000000000000000000000000000000000000000000000000000000000000000000";

    static {
        INVERTED_ALPHABET = new int[128];
        for (int i = 0; i < 128; i++) {
            INVERTED_ALPHABET[i] = -1;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            INVERTED_ALPHABET[i] = (i - 'A' + 10);
        }
        for (int i = '0'; i <= '9'; i++) {
            INVERTED_ALPHABET[i] = (i - '0');
        }
    }

    static final BigInteger MODULUS = new BigInteger("36");

    /**
     * Hex编码。一个字节对应2个字符，返回的字符串长度=2*入口参数的长度。
     *
     * @param data 十六进制字节数组。
     * @return 编码后的字符串。如果hexData为null或长度为0，返回null。
     */
    public static String hexEncode(byte[] data) {
        if (data == null || data.length == 0)
            return null;

        return Hex.encodeHexString(data);
    }

    /**
     * Hex解码.
     *
     * @param str 待解码的字符串。
     * @return 解码后的十六进制字节数组。如果str为null或长度为0，返回null。
     * @throws DecoderException
     */
    public static byte[] hexDecode(String str) throws DecoderException {
        byte[] result = null;
        if (str == null || str.length() == 0)
            return result;

        //str是否是十六进制字符串
        return Hex.decodeHex(str.toCharArray());

    }

    /**
     * Base64编码.
     *
     * @param binaryData 待编码的字节数组。
     * @return 编码后的字符串。如果input为null或长度为0，返回null。
     */
    public static String base64Encode(byte[] binaryData) {
        if (binaryData == null || binaryData.length == 0)
            return null;

        return org.apache.commons.codec.binary.Base64.encodeBase64String(binaryData);
    }

    /**
     * URL Base64编码, URL安全(对URL不支持的字符如+,/,=转为其他字符, 见RFC3548).
     *
     * @param binaryData binary data to encode
     * @return String containing Base64 characters。如果binaryData为null或长度为0，返回null.
     */
    public static String base64UrlEncode(byte[] binaryData) {
        if (binaryData == null || binaryData.length == 0)
            return null;

        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(binaryData);
    }

    /**
     * Base64解码.
     *
     * @param base64String 待解码的字符串。
     * @return Array containing decoded data.如果base64String为null或长度为0，返回null
     */
    public static byte[] base64Decode(String base64String) {
        if (base64String == null || base64String.length() == 0)
            return null;

        return org.apache.commons.codec.binary.Base64.decodeBase64(base64String);
    }

    /**
     * base36编码。
     *
     * @param hexStr 十六进制字符串。
     * @return 经base36编码后的字符串。如果hexStr为null或长度为0，返回null。
     */
    public static String base36Encode(String hexStr) {
        if (hexStr == null || hexStr.length() == 0)
            return null;

        StringBuffer sb = new StringBuffer();

        BigInteger d = new BigInteger(hexStr, 16);
        BigInteger m = BigInteger.ZERO;

        while (!BigInteger.ZERO.equals(d)) {
            m = d.mod(MODULUS);
            d = d.divide(MODULUS);
            sb.insert(0, ALPHABET[m.intValue()]);
        }
        return sb.toString();
    }

    /**
     * base36编码。
     * <p>
     * 例如：
     * String ori = "91a6631029ce0aaad";
     * String test1 = Base36.encode(ori, 12);
     * String test2 = Base36.encode(ori, 20);
     * 得到test1是12位长的，test2是20位长：
     * 91a6631029ce0aaad FSSOSD2L8XWT
     * 91a6631029ce0aaad 0000000ZFSSOSD2L8XWT
     *
     * @param hexStr 十六进制字符串。
     * @param length 转码后的串长度。
     * @return 返回指定长度的信息。如果hexStr为null或长度为0，返回null。
     */
    public static String base36Encode(String hexStr, int length) {

        if (hexStr == null || hexStr.length() == 0)
            return null;

        if (length > 64) {
            throw new IllegalArgumentException("参数错误（length不能超过64）:param length = "
                    + length);
        }
        String str = base36Encode(hexStr);
        if (length > str.length()) {
            str = INIT_STR.concat(str);
            str = str.substring(str.length() - length);
        } else {
            str = str.substring(str.length() - length);
        }
        return str;
    }

    /**
     * Base36解码。
     *
     * @param base36Str base36字符串。
     * @return 解码后的十六进制字符串。如果base36Str为null或长度为0，返回null。
     */
    public static String base36Decode(String base36Str) {

        //参数校验。
        if (base36Str == null || base36Str.length() == 0)
            return null;

        if (!isValidBase32Str(base36Str)) {
            throw new IllegalArgumentException("base36字符串格式错误:\"" + base36Str + "\"");
        }

        //转换成大写。
        base36Str = base36Str.toUpperCase();

        BigInteger bi = BigInteger.ZERO;

        char[] chars = base36Str.toCharArray();

        for (int i = 0; i < base36Str.length(); i++) {
            char c = chars[i];
            int n = INVERTED_ALPHABET[c];
            bi = bi.multiply(MODULUS).add(new BigInteger("" + n));
        }

        return bi.toString(16);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     *
     * @param s String to be translated
     * @return the translated String.  如果s为null或内容为空，返回null。
     */
    public static String urlEncode(String s) {
        return urlEncode(s, DEFAULT_URL_ENCODING);
    }

    /**
     * URL 编码.
     *
     * @param s   String to be translated
     * @param enc The name of a supported character encoding
     * @return the translated String.如果s为null或内容为空，返回null。
     */
    public static String urlEncode(String s, String enc) {
        if (s == null || s.trim().length() == 0)
            return null;

        try {
            return URLEncoder.encode(s, enc);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("urlEncode(" + s + "," + enc + ")，URL编码异常!", e);
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     *
     * @param encodedUrl 待解码的编码url。
     * @return 解码后的url。如果encodedUrl为null或字符串里面没有字符，返回null。
     */
    public static String urlDecode(String encodedUrl) {
        return urlDecode(encodedUrl, DEFAULT_URL_ENCODING);
    }

    /**
     * URL 解码.
     *
     * @param s   the String to decode
     * @param enc The name of a supported character encoding.
     * @return the newly decoded String.如果s为null或内容为空，返回null。
     */
    public static String urlDecode(String s, String enc) {
        if (s == null || s.trim().length() == 0)
            return s;

        try {
            return URLDecoder.decode(s, enc);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("urlEncode(" + s + "," + enc + ")，URL解码异常!", e);
        }
    }

    /**
     * Xml转码.
     *
     * @param xml 待转码的xml代码。
     * @return 转码后的字符串。如果xml为null或长度为0，返回null。
     */
    public static String xmlEscape(String xml) {
        if (xml == null || xml.length() == 0)
            return null;

        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * Xml 反转码.
     *
     * @param escapedXml 待反转码的xml字符串。
     * @return 反转码后的字符串。如果escapedXml为null或长度为0，返回null。
     */
    public static String xmlUnescape(String escapedXml) {
        if (escapedXml == null || escapedXml.length() == 0)
            return null;

        return StringEscapeUtils.unescapeXml(escapedXml);
    }

    /**
     * 判断是否是Base32字符串。
     *
     * @param str
     * @return
     */
    private static boolean isValidBase32Str(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isValidBase32Char(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是base32字符。
     *
     * @param c
     * @return
     */
    private static boolean isValidBase32Char(char c) {
        if ((c < 0) || (c >= 128)) {
            return false;
        } else if (INVERTED_ALPHABET[c] == -1) {
            return false;
        }
        return true;
    }
}
