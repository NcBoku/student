package com.dxy.Test;

import com.dxy.mapper.ClazzMapper;
import com.dxy.pojo.Clazz;
import com.dxy.service.ClazzService;
import com.dxy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashMap;

@SpringBootTest
public class TestCases {

    @Autowired
    private UserService userService;

    @Autowired
    private ClazzService clazzService;

    @Test
    public void testUserLogin() {
        Assert.isTrue(0 == userService.login("admin_t", "test111", 0), "admin");
        Assert.isTrue(1 == userService.login("teacher_t", "test111", 1), "teacher");
        Assert.isTrue(2 == userService.login("student_t", "test111", 2), "student");
        Assert.isTrue(-1 == userService.login("student_t", "test111", 0), "not found");
    }

    @Test
    public void testAdminOperateClazz(){

    }

    @Test
    public void testAdminGetGrades() {

    }
}
