package com.kennyzhu.wx.core.service;

import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WeiXinPublicAccessTokenCache {
    private static Map<String, WeiXinPublicAccessToken> cache = new ConcurrentHashMap<>();
    private static WeiXinPublicAccessTokenCache INSTANCE = new WeiXinPublicAccessTokenCache();

    private WeiXinPublicAccessTokenCache() {
    }

    public static WeiXinPublicAccessTokenCache getInstance() {
        return INSTANCE;
    }

    public WeiXinPublicAccessToken get(String appId) {
        return cache.get(appId);
    }

    public synchronized boolean set(String appId, WeiXinPublicAccessToken accessToken) {
        cache.put(appId, accessToken);
        return true;
    }


}
