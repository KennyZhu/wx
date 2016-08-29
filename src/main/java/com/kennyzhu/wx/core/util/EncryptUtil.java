package com.kennyzhu.wx.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public final class EncryptUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    private static final String ALGORITHM = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    private static final byte[] ENCODE_STR =
            {(byte) 0xef, 0x2b, (byte) 0xcc, (byte) 0xdc, (byte) 0x9b, 0x3b, (byte) 0xf7, 0x2a, 0x68, (byte) 0xad,
                    (byte) 0xeb, 0x72, (byte) 0xe3, 0x78, 0x2f, 0x5e, 0x7, 0x77, (byte) 0xd5, (byte) 0xc1, 0x7d, 0x40, 0x66,
                    (byte) 0xb8};//解密秘钥

    /**
     * 加密算法
     *
     * @param source
     * @param timestamp
     * @return
     */
    public static String encryptCode(String source, String timestamp) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(timestamp)) {
            return null;
        }
        byte[] encodeKeys = EncryptUtil.genCroptyKey(ENCODE_STR, timestamp);
        return getBASE64(encryptMode(encodeKeys, source.getBytes()));

    }

    /**
     * 加密算法
     *
     * @param keyByte    为加密密钥，长度为24字节
     * @param sourceByte 为被加密的数据缓冲区（源）
     * @return
     */
    public static byte[] encryptMode(byte[] keyByte, byte[] sourceByte) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyByte, ALGORITHM);
            //加密
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(sourceByte);//在单一方面的加密或解密
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密算法
     *
     * @param miyao   为加密密钥，长度为24字节
     * @param srcByte src为加密后的缓冲区
     * @return
     */
    public static byte[] decryptMode(byte[] miyao, byte[] srcByte) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(miyao, ALGORITHM);
            //解密
            Cipher c1 = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5Padding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(srcByte);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 生成秘钥
     *
     * @param randomStrB
     * @return
     */
    public static byte[] genCroptyKey(String randomStrB) {
        return genCroptyKey(ENCODE_STR, randomStrB);
    }

    /*
     *生成密钥 encodeKeyA.length=24
     */
    private static byte[] genCroptyKey(byte[] encodeKeyA, String randomStrB) {
        if (encodeKeyA == null) {
            return null;
        }
        byte[] A = encodeKeyA;
        byte[] B = new byte[24];
        byte[] C = randomStrB.getBytes();
        int alen = A.length;
        int clen = C.length;
        if (alen != 24 || (clen < 8 || clen > 20))
            return null;
        int demension = alen - clen;
        System.arraycopy(C, 0, B, 0, C.length);

        int piont = 1;
        while (demension > 0) {
            if (demension > clen) {
                System.arraycopy(C, 0, B, clen * piont + 0, clen);
                piont++;

            } else {
                System.arraycopy(C, 0, B, clen * piont + 0, demension);
            }
            demension = demension - clen;

        }
        System.out.println("######" + getBASE64(B));
        byte[] result = new byte[24];
        for (int i = 0; i < alen; i++) { //0 ^  1 |  2 &

            switch ((i + 1) % 3) {
                case 0:
                    result[i] = (byte) (A[i] ^ B[i]);
                    break;
                case 1:
                    result[i] = (byte) (A[i] & B[i]);
                    break;
                case 2:
                    result[i] = (byte) (A[i] | B[i]);
                    break;
            }

        }
        return result;
    }

    public static String getBASE64(byte[] b) {
        String s = null;
        if (b != null) {
            s = new sun.misc.BASE64Encoder().encode(b).replaceAll("\r|\n", "");
        }
        return s;
    }

    public static byte[] getFromBASE64(String s) {
        byte[] b = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 传输加密参数，组合
     *
     * @param oraStr
     * @return
     */

    public static String restructParam(String oraStr) {

        try {
            byte[] keys = EncryptUtil.ENCODE_STR;
            if (StringUtils.isNotBlank(oraStr)) {

                String stampStr = EncryptUtil.RndString(10, null);
                byte[] encodeKeys = EncryptUtil.genCroptyKey(keys, stampStr);
                byte[] result = EncryptUtil.encryptMode(encodeKeys, oraStr.getBytes());
                String base64Str = EncryptUtil.getBASE64(result);
                try {
                    String data = URLEncoder.encode(base64Str, "utf-8");
                    String lastStr = data + "&stamp=" + URLEncoder.encode(stampStr, "utf-8");
                    return lastStr;
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("UnsupportedEncodingException:", e);
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error in restructParam", e);
        }
        return null;

    }

    /**
     * 生成随机字符串
     */
    public static String RndString(int Length, int[] Seed) {
        String strSep = ",";
        // char[] chrSep = strSep.ToCharArray();

        //这里定义字符集
        String strChar = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z"
                + ",A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,W,X,Y,Z";

        String[] aryChar = strChar.split(strSep, strChar.length());

        String strRandom = "";
        Random Rnd;
        if (Seed != null && Seed.length > 0) {
            Rnd = new Random(Seed[0]);
        } else {
            Rnd = new Random();
        }

        //生成随机字符串
        for (int i = 0; i < Length; i++) {
            strRandom += aryChar[Rnd.nextInt(aryChar.length)];
        }

        return strRandom;
    }


    public static void main(String[] args) {
        //apiVer=1.1&data=&stamp=4658813633344
        try {
//            String stamp = "4658813633344";
            String stamp = "1453536007";
            byte[] miyao = EncryptUtil.genCroptyKey(ENCODE_STR, stamp);

            String decode = "G7fVJKt+qmZLnK8N4R867lOyLGsOURtI";
            System.out.println("#Encode is " + decode);
            String baseStr = decode;
            System.out.println("base64加密后的字符串:" + baseStr + "  miyao is " + EncryptUtil.getBASE64(miyao));
            System.out.println("---------hhh-----------------");

//            String deCode = "xAPRtRAkGMYl9ibjVtzOxvXnf1i3FtIzZucEKu0M27BhCLyKJlYoE40DUFcc4LRWpes94f2w7xnaeUaRF8vwJstXHSrlIjJ8";
            byte[] base = EncryptUtil
                    .getFromBASE64(baseStr);

            byte[] decodeBytes = EncryptUtil.decryptMode(miyao, base);

            String decodeStr = new String(decodeBytes, "utf-8");
            System.out.println("base64解密后的字符串:" + decodeStr);
        } catch (Exception e) {

        }
    }
}
