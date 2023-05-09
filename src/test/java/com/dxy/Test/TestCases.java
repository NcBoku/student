package com.dxy.Test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Exam;
import com.dxy.pojo.User;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.ExamPageResponse;
import com.dxy.service.*;
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

    @Autowired
    private ClazzroomService clazzroomService;

    @Test
    public void test10() {
        String s="a";
        String b="a,b,c";
        for (String s1 : s.split(",")) {
            System.out.println(s1);
        }
        for (String s1 : b.split(",")) {
            System.out.println(s1);
        }
    }
}
