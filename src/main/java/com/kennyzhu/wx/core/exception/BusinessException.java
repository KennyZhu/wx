package com.kennyzhu.wx.core.exception;

public class BusinessException extends Exception {
    public BusinessException(Object message, Exception e) {
        super(message.toString(), e);
    }

    public BusinessException(Object message) {
        super(message.toString());
    }

}
