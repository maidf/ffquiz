package com.ff.common.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(200, "成功"),
    ERROR(444, "错误"),
    EXCEPTION(555, "异常");

    private final int code;
    private final String msg;

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}