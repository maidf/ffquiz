package com.maidf.javaquiz.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmailMsgEnum {
    RESET_PASSWORD(0, "您正在进行修改密码操作，验证码有效期5分钟，验证码为："),
    LOGOFF(1, "您正在进行账号注销操作，验证码有效期5分钟，验证码为：");

    private final Integer type;
    private final String msg;

    public Integer type() {
        return type;
    }

    public String msg() {
        return msg;
    }
}
