package com.ff.common.service;

import org.apache.commons.mail.EmailException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ff.common.entity.dto.SessionUserDto;
import com.ff.common.entity.dto.UpdatePasswordDto;
import com.ff.common.entity.enums.EmailMsgEnum;
import com.ff.common.entity.po.User;

public interface UserService extends IService<User> {
    void register(String account, String password, Boolean isTeacher) throws Exception;

    SessionUserDto login(String account, String password) throws Exception;

    String sendEmail(String key, String to, EmailMsgEnum emailMsgEnum) throws EmailException;

    void updatePassword(UpdatePasswordDto updatePasswordDto, User user) throws Exception;

    void logoff(String token, String code) throws Exception;

    void logout(String token) throws Exception;
}
