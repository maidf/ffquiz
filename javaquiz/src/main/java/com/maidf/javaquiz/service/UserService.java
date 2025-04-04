package com.maidf.javaquiz.service;

import org.apache.commons.mail.EmailException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maidf.javaquiz.entity.dto.SessionUserDto;
import com.maidf.javaquiz.entity.enums.EmailMsgEnum;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.entity.req.UpdatePasswordReq;

public interface UserService extends IService<User> {
    void register(String account, String password, Boolean isTeacher) throws Exception;

    SessionUserDto login(String account, String password) throws Exception;

    String sendEmail(String key, String to, EmailMsgEnum emailMsgEnum) throws EmailException;

    void updatePassword(UpdatePasswordReq entity, User user) throws Exception;

    void logoff(String token, String code) throws Exception;

    void logout(String token) throws Exception;
}
