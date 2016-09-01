package com.kennyzhu.wx.core.service;

import com.kennyzhu.wx.core.model.WeiXinPublicMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Desc:
 * <br/>User: ylzhu
 */
@Service
public class WeiXinPublicSubEventHandler implements WeiXinPublicEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinPublicSubEventHandler.class);

    @Override
    public void handle(WeiXinPublicMsg msg, Map<String, Object> params) {
        if (msg == null) {
            return;
        }
    }
}
