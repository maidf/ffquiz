package com.maidf.javaquiz.service.impl;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maidf.javaquiz.entity.dto.SessionUserDto;
import com.maidf.javaquiz.entity.enums.EmailMsgEnum;
import com.maidf.javaquiz.entity.enums.UserRoleEnum;
import com.maidf.javaquiz.entity.po.User;
import com.maidf.javaquiz.entity.req.UpdatePasswordReq;
import com.maidf.javaquiz.mapper.UserMapper;
import com.maidf.javaquiz.service.UserService;
import com.maidf.javaquiz.util.EmailSender;
import com.maidf.javaquiz.util.EncryUtil;
import com.maidf.javaquiz.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @Autowired
    private JwtUtil jwtUtil;

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
            log.info("redis_code:{}", code);
            if (code == null) {
                throw new Exception("验证码过期");
            }
            if (input.equals(code)) {
                log.info("delete_redis_code:{}_{}", key, code);
                redis.delete(key);
            } else {
                throw new Exception("验证码错误");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updatePassword(UpdatePasswordReq entity, User user) throws Exception {
        String hashOldPassword = encryUtil.hash(entity.getOldPassword());
        String hashNewPassword = encryUtil.hash(entity.getNewPassword());
        if (!user.getPassword().equals(hashOldPassword)) {
            throw new Exception("密码错误");
        }
        try {
            verifyCode(user.getId().toString() + EmailMsgEnum.RESET_PASSWORD.type(), entity.getCode());
        } catch (Exception e) {
            throw e;
        }
        user.setPassword(hashNewPassword);
        userMapper.updateById(user);
    }

    @Override
    public void logoff(String token, String code) throws Exception {
        Long userId = jwtUtil.getLoginUserId(token);

        try {
            String key = userId.toString() + EmailMsgEnum.LOGOFF.type();
            log.info("logoff_key:{}, logoff_input_code:{}", key, code);
            verifyCode(key, code);
        } catch (Exception e) {
            throw e;
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId)
                .set("account", null)
                .set("email", null)
                .set("name", "用户已注销");
        userMapper.update(null, updateWrapper); // 强制更新

        logout(token);
    }

    @Override
    public void logout(String token) throws Exception {
        Long id = jwtUtil.getLoginUserId(token);
        // 解析 JWT 获取有效期
        Date expirationDate = jwtUtil.getExpireDate(token);

        // 计算过期时间，单位是秒
        long expirationTime = expirationDate.getTime() - System.currentTimeMillis();
        long expirationInSeconds = expirationTime / 1000; // 转换为秒

        // 设置 Redis 键值对并设置过期时间
        redis.opsForSet().add("logout" + id, token);
        redis.expire("logout" + id, expirationInSeconds, TimeUnit.SECONDS);
    }
}
