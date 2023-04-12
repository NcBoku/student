package com.dxy.Test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Exam;
import com.dxy.pojo.User;
import com.dxy.request.ExamScoreRequest;
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

import javax.sound.midi.Soundbank;
import java.util.Date;

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


    }
}
