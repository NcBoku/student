package com.dxy.Test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.pojo.Exam;
import com.dxy.pojo.User;
import com.dxy.response.ExamPageResponse;
import com.dxy.service.ClazzService;
import com.dxy.service.ExamService;
import com.dxy.service.UserService;
import com.dxy.util.UserUtil;
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

    @Autowired
    private ExamService examService;

    @Test
    public void test10() {
        User user = new User();
        user.setId(88);
        user.setType(1);
        UserUtil.set("test", user);
        Page<Exam> page = new Page<>();
        ExamPageResponse list = examService.list(page, "test");
        System.out.println(list);
    }
}
