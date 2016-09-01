package com.kennyzhu.wx.core.service;

import com.kennyzhu.wx.core.model.AccessToken;
import com.kennyzhu.wx.core.model.WeiXinPublicAccessToken;

/**
 * Created by ylzhu on 2016/7/7.
 */
public interface WeiXinService {

    public AccessToken getUserAccessToken(String code);

    /**
     * @return
     */
    public WeiXinPublicAccessToken getPublicAccessToken();

    /**
     * @param openId
     * @return
     */
    public WeiXinUserInfo getWeiXinUserInfo(String openId);


    /**
     * @param accessToken
     * @return
     */
    public boolean createWeiXinPublicMenu(String accessToken);


    public boolean deleteWeiXinPublicMenu(String accessToken);

    /**
     * 发送消息
     *
     * @param openId
     * @param content
     * @return
     */
    public String sendTextMsgToUser(String openId, String content);

}
