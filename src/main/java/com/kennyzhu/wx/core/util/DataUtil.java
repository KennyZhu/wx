package com.kennyzhu.wx.core.util;


import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: KennyZhu
 * Date: 16/7/2
 * Desc:
 */
public final class DataUtil {
    private DataUtil() {
    }

    public static boolean checkPageParam(int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 参数字符串中解析参数对
     *
     * @param paramStr
     * @return
     */
    public static Map<String, String> parseParamStr(String paramStr) {
        if (StringUtils.isBlank(paramStr)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        try {
            String[] params = paramStr.split("\\&");
            //验证参数是否符合规定的字符串
            for (String str : params) {
                String[] _pairs = str.split("\\=");
                if (_pairs.length == 2) {
                    result.put(_pairs[0], URLDecoder.decode(_pairs[1], "UTF-8"));
                }
            }
            return result;
        } catch (Exception e) {
            return Collections.emptyMap();
        }

    }
}
