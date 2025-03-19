package com.ff.teacher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ff.common.entity.enums.UserRoleEnum;
import com.ff.common.entity.po.User;
import com.ff.common.mapper.UserMapper;

@SpringBootTest
class TeacherApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        User user = new User(null, "sss", "123", "123@qq.com", UserRoleEnum.STUDENT, null, null);
        userMapper.insert(user);
    }

}
