package com.kennyzhu.wx.core.service;

/**
 * Desc:
 * <p/>Date: 2015-10-15
 * <br/>Time: 14:02
 * <br/>User: ylzhu
 */
public interface HttpService {

    /**
     * @param url
     * @param params
     * @return
     */
    public String sendPostRequest(String url, String params);

    /**
     * 发送Post请求
     *
     * @return
     */
    public String sendPostRequest(String url);

    /**
     * 发送 get请求Https
     *
     * @param url
     */
    public String sendGetRequest(String url);

}
