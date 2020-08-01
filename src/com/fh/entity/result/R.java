package com.fh.entity.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：统一结果类，所有请求返回统一规格
 * 创建人：Ajie
 * 创建时间：2020年4月20日14:39:00
 */
@Data
public class R {

    // 响应 true/false
    private Boolean success;
    // 响应 状态码
    private Integer code;
    // 响应 成功、失败等提示
    private String message;
    // 响应数据 JSON 格式
    private Map<String, Object> data = new HashMap<>();

    // 构造器私有。外界只可以调用统一返回类的方法，不可以直接创建，因此构造器私有
    private R() {}

    /**
     * 功能描述：通用返回成功
     *
     * @author Ajie
     * @date 2020-4-20 14:52:30
     */
    public static R ok() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 功能描述：通用返回失败，未知错误
     *
     * @author Ajie
     * @date 2020-4-20 14:52:21
     */
    public static R error() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    /**
     * 功能描述：返回失败，参数错误
     *
     * @author Ajie
     * @date 2020-4-27 11:17:56
     */
    public static R parError() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.PARAM_ERROR.getSuccess());
        r.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
        r.setMessage(ResultCodeEnum.PARAM_ERROR.getMessage());
        return r;
    }

    /**
     * 功能描述：设置结果，形参为结果枚举
     *
     * @author Ajie
     * @date 2020-4-20 14:56:39
     */
    public static R setResult(ResultCodeEnum result) {
        R r = new R();
        r.setMessage(result.getMessage());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**
     * ------------使用链式编程，返回类本身-----------
     **/

    // 自定义返回数据
    public R data(Map<String, Object> map) {
        this.data = map;
        return this;
    }

    // 通用设置data
    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public R success(Boolean succcess) {
        this.setSuccess(succcess);
        return this;
    }

}
