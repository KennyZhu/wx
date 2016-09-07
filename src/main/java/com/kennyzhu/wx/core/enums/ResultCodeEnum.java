package com.kennyzhu.wx.core.enums;

import java.util.stream.Stream;

/**
 * User: KennyZhu
 * Date: 16/6/27
 * Desc:
 */
public enum ResultCodeEnum {


    SUCCESS(100, "success"),

    FAIL(101, "fail"),

    EXCEPTION(-1, "exception"),

    INVALID_PARAM(-2, "invalid param");


    private final int code;
    private final String desc;

    ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResultCodeEnum getByCode(int code) {
        return Stream.of(ResultCodeEnum.values()).filter(resultCodeEnum -> resultCodeEnum.getCode() == code).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
