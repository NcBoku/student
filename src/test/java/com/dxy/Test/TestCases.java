package com.dxy.Test;

import com.dxy.service.ClazzService;
import com.dxy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class TestCases {

    @Autowired
    private UserService userService;

    @Autowired
    private ClazzService clazzService;

    @Test
    public void testUserLogin() {

    }

    @Test
    public void testAdminOperateClazz(){

    }

    @Test
    public void testAdminGetGrades() {

    }
}
