package com.kennyzhu.wx.core.common;

import com.kennyzhu.wx.core.enums.ResultCodeEnum;
import com.kennyzhu.wx.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * User: KennyZhu
 * Date: 16/6/27
 * Desc:
 */
@Controller
public class BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);


    /**
     * @param resultData
     * @return
     */
    protected String buildSuccessJson(Object resultData) {
        return buildSuccessJson(new BaseResult(resultData));
    }

    /**
     * @param result
     * @return
     */
    protected String buildSuccessJson(BaseResult result) {
        result = (result == null ? new BaseResult(ResultCodeEnum.SUCCESS) : result);
        return JsonUtil.getJsonString(result);
    }

    /**
     * @return
     */
    protected String buildSuccessJson() {
        return buildSuccessJson(null);
    }

    /**
     * 参数错误
     *
     * @return
     */
    protected String buildInvalidParamJson() {
        return JsonUtil.getJsonString(new BaseResult(ResultCodeEnum.INVALID_PARAM));
    }

    /**
     * 异常
     *
     * @return
     */
    protected String buildExceptionJson() {
        return JsonUtil.getJsonString(new BaseResult(ResultCodeEnum.EXCEPTION));
    }

    /**
     * 操作失败
     *
     * @param failDesc
     * @return
     */
    protected String buildFailJson(String failDesc) {
        return JsonUtil.getJsonString(new BaseResult(ResultCodeEnum.FAIL.getCode(), failDesc));
    }

    /**
     * @param result
     * @return
     */
    protected String buildFailJson(BaseResult result) {
        result = (result == null ? new BaseResult(ResultCodeEnum.FAIL) : result);
        return JsonUtil.getJsonString(result);
    }

    /**
     * @return
     */
    protected String buildFailJson() {
        return buildFailJson(ResultCodeEnum.FAIL.getDesc());
    }

    /**
     * 未登录
     *
     * @return
     */
    protected String buildNoLogin() {
        return JsonUtil.getJsonString(new BaseResult(ResultCodeEnum.NOT_LOGIN));
    }

    /**
     * @param resultCodeEnum
     * @return
     */
    protected String buildFailJson(ResultCodeEnum resultCodeEnum) {
        return JsonUtil.getJsonString(new BaseResult(resultCodeEnum));
    }


    protected boolean checkPageParam(int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            return false;
        }
        return true;
    }
}
