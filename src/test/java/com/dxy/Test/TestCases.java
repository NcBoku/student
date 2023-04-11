package com.dxy.Test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Exam;
import com.dxy.pojo.User;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.ExamPageResponse;
import com.dxy.service.ClazzService;
import com.dxy.service.ExamService;
import com.dxy.service.StudentService;
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
    private StudentService studentService;

    @Autowired
    private ExamService examService;

    @Test
    public void test10() {
        User user = new User();
        user.setId(87);
        user.setType(1);
        UserUtil.set("test", user);
        StudentUpdateRequest request = new StudentUpdateRequest();
        request.setName("修改后的名称");
        request.setSex("男");
        request.setPhone("10077");
        request.setQq("99001");
        System.out.println("xxxx:" + studentService.update(request, "test"));

    }
}
