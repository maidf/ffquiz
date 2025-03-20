package com.ff.common.service.impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.common.entity.dto.SessionUserDto;
import com.ff.common.entity.dto.UpdatePasswordDto;
import com.ff.common.entity.enums.EmailMsgEnum;
import com.ff.common.entity.enums.UserRoleEnum;
import com.ff.common.entity.po.User;
import com.ff.common.mapper.UserMapper;
import com.ff.common.service.UserService;
import com.ff.common.util.EmailSender;
import com.ff.common.util.EncryUtil;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EncryUtil encryUtil;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RedisTemplate<String, String> redis;

    @Override
    public void register(String account, String password, Boolean isTeacher) throws Exception {
        String hashPassword = encryUtil.hash(password);
        User user = new User();
        user.setAccount(account);
        user.setPassword(hashPassword);
        user.setName(account);
        if (isTeacher) {
            user.setRole(UserRoleEnum.TEACHER);
        } else {
            user.setRole(UserRoleEnum.STUDENT);
        }
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            throw new Exception("账号已存在");
        }
    }

    @Override
    public SessionUserDto login(String account, String password) throws Exception {
        String hashPassword = encryUtil.hash(password);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User user = userMapper.selectOne(wrapper.eq(User::getAccount, account));

        if (user.getAccount() == null) {
            throw new Exception("账号密码错误");
        }

        if (!user.getPassword().equals(hashPassword)) {
            throw new Exception("账号密码错误");
        }

        SessionUserDto sessionUser = new SessionUserDto();
        sessionUser.setId(user.getId());
        sessionUser.setName(user.getName());
        sessionUser.setIsTeacher(user.getRole().equals(UserRoleEnum.TEACHER));
        return sessionUser;
    }

    @Override
    public String sendEmail(String key, String to, EmailMsgEnum emailMsgEnum) throws EmailException {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        emailSender.sendEmail(to, emailMsgEnum.msg() + code.toString());
        redis.opsForValue().set(key + emailMsgEnum.type(), code.toString(), 5, TimeUnit.MINUTES);
        return code.toString();
    }

    private void verifyCode(String key, String input) throws Exception {
        try {
            String code = redis.opsForValue().get(key);
            if (input.equals(code)) {
                redis.delete(key);
            } else {
                throw new Exception("验证码错误");
            }
        } catch (Exception e) {
            throw new Exception("验证码过期");
        }
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto, User user) throws Exception {
        String hashOldPassword = encryUtil.hash(updatePasswordDto.getOldPassword());
        String hashNewPassword = encryUtil.hash(updatePasswordDto.getNewPassword());
        if (!user.getPassword().equals(hashOldPassword)) {
            throw new Exception("密码错误");
        }
        try {
            verifyCode(user.getId().toString() + EmailMsgEnum.RESET_PASSWORD.type(), updatePasswordDto.getCode());
        } catch (Exception e) {
            throw e;
        }
        user.setPassword(hashNewPassword);
        userMapper.updateById(user);
    }

    @Override
    public void logoff(Integer userId, String code) throws Exception {

        try {
            verifyCode(userId.toString() + EmailMsgEnum.LOGOFF.type(), code);
        } catch (Exception e) {
            throw e;
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId)
                .set("account", null)
                .set("email", null)
                .set("name", "用户已注销");
        userMapper.update(null, updateWrapper); // 强制更新
    }
}
