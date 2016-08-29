package com.kennyzhu.wx.core.common;

import com.kennyzhu.wx.core.enums.ResultCodeEnum;

/**
 * User: KennyZhu
 * Date: 16/6/27
 * Desc:
 */
public class BaseResult {
    private int result;
    private String resultDesc;
    private Object resultData;

    public BaseResult(int result, String resultDesc) {
        this.result = result;
        this.resultDesc = resultDesc;
        this.resultData = "";
    }

    public BaseResult(ResultCodeEnum resultCodeEnum) {
        if (resultCodeEnum != null) {
            this.result = resultCodeEnum.getCode();
            this.resultDesc = resultCodeEnum.getDesc();
            this.resultData = "";
        }
    }

    public BaseResult(Object resultData) {
        this.result = ResultCodeEnum.SUCCESS.getCode();
        this.resultDesc = ResultCodeEnum.SUCCESS.getDesc();
        this.resultData = resultData;
    }

    public BaseResult() {

    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "result=" + result +
                ", resultDesc='" + resultDesc + '\'' +
                ", resultData=" + resultData +
                '}';
    }
}
