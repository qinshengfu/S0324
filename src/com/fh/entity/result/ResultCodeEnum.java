package com.fh.entity.result;

import lombok.Getter;

/**
 * 说明：结果类枚举
 * 创建人：Ajie
 * 创建时间：2020年4月20日14:18:39
 */
@Getter
public enum ResultCodeEnum {

    // 返回成功
    SUCCESS(true, 20000, "成功"),
    // 返回未知错误
    UNKNOWN_ERROR(false, 20001, "未知错误"),
    // 返回参数错误
    PARAM_ERROR(false, 20002, "参数错误"),
    ;

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }


}
