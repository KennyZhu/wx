package com.kennyzhu.wx.core.util;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Desc:
 * <p/>Date: 2015-11-05
 * <br/>Time: 17:31
 * <br/>User: ylzhu
 */
public class MD5Util {

    public static final String DEFAULT_ENCODE = "UTF-8";

    /**
     * 获取字符串S的MD5摘要串
     *
     * @param s      源串
     * @param encode 编码类型，根据合作方的约定来定，例如"GBK"，"UTF-8"等
     * @return MD5摘要串
     */
    public static String get(String s, String encode) {
        byte abyte0[] = null;
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(s.getBytes(encode));
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new IllegalArgumentException("no md5 support");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持" + encode + "编码");
        }
        abyte0 = messagedigest.digest();
        return CodecUtil.hexEncode(abyte0);
    }

    /**
     * 使用 MD5 哈希函数计算基于哈希值的消息验证代码 (HMAC)。 HMACMD5 是从 MD5
     * 哈希函数构造的一种键控哈希算法，被用作基于哈希的消息验证代码 (HMAC)。 此 HMAC
     * 进程将密钥与消息数据混合，使用哈希函数对混合结果进行哈希计算，将所得哈希值与该密钥混合，然后再次应用哈希函数。 输出的哈希值长度为 128 位。
     *
     * @param ssn
     * @param seed
     * @return 摘要串
     */
    public static String hmacmd5(String ssn, int seed) {
        try {
            byte[] plainText = ssn.getBytes();

            KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
            SecureRandom sr = new SecureRandom();

            // LoggerUtil.debug("seed=" + seed, "SecurityUtil", "hmacmd5");

            sr.setSeed(seed);
            keyGen.init(64, sr);
            SecretKey MD5key = keyGen.generateKey();
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(MD5key);
            mac.update(plainText);

            return URLEncoder.encode(new String(mac.doFinal()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5摘要值校验，根据合作方传来的摘要串和原始串，校验摘要值的合法性
     *
     * @param password    摘要串
     * @param inputString 原始串
     * @param codeType    编码类型
     * @return MD5校验结果
     */
    public static boolean validate(String password, String inputString,
                                   String codeType) {
        System.out.println("sign:" + password + "，根据源串：" + inputString + "，生成的签名串:" + digest(inputString, codeType));
        return password.equalsIgnoreCase(digest(inputString, codeType));
    }

    /**
     * 计算字符串originString的MD5摘要串
     *
     * @param originString 源串
     * @param codeType     编码类型，根据合作方的约定来定，例如"GBK"，"UTF-8"等
     * @return MD5摘要串
     */
    private static String digest(String originString, String codeType) {
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                // LoggerUtil.debug("源串："+originString);
                byte[] results = md.digest(originString.getBytes(codeType));
                // 将得到的字节数组变成字符串返回
                String resultString = CodecUtil.hexEncode(results);
                // MD5结果转换成大写字母
                String pass = resultString.toUpperCase();
                // LoggerUtil.debug("结果串："+ pass);
                return pass;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String source = "cp_client";
        System.out.println("####" + MD5Util.get(source, "UTF-8"));
    }
}