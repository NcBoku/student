package com.dxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.CourseMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.pojo.*;
import com.dxy.request.TeacherUpdateRequest;
import com.dxy.response.StudentExcel;
import com.dxy.response.TeacherExcel;
import com.dxy.response.UpdateResponse;
import com.dxy.service.*;
import com.dxy.util.ExcelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
@CrossOrigin
public class ExcelController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @GetMapping("/score")
    public void score(HttpServletResponse response) {
        System.out.println("开始");
        scoreService.getScoreExcelData(response);

    }

    @GetMapping("/score/ids")
    public void score(List<Integer> ids, HttpServletResponse response) {
        System.out.println("开始");
        scoreService.getScoreExcelData(ids, response);

    }

    @PostMapping("/upload/student")
    @Transactional
    public UpdateResponse student(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("X-Token") String token) {
        UpdateResponse response = new UpdateResponse();
        List<StudentExcel> studentExcels = ExcelUtil.importStudentExcel(multipartFile);
        for (StudentExcel e : studentExcels) {
            Student student = new Student();
            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getName, e.getGrade()));
            if (grade == null) {
                response.setError("该年级不存在" + e.getGrade() + "不存在");
                return response;
            }
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getName, e.getClazz()).eq(Clazz::getGradeId, grade.getId()));
            if (clazz == null) {
                response.setError("该班级不存在" + e.getClazz() + "不存在");
                return response;
            }
            BeanUtils.copyProperties(e, student);
            student.setGradeId(grade.getId());
            student.setClazzId(clazz.getId());
            studentService.insert(student, token);

        }
        response.setCode(20000);
        response.setError("上传完毕");
        return response;
    }

    @Transactional
    @PostMapping("/upload/teacher")
    public UpdateResponse teacher(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("X-Token") String token) {
        UpdateResponse response = new UpdateResponse();
        List<TeacherExcel> teacherExcels = ExcelUtil.importTeacherExcel(multipartFile);
        for (TeacherExcel e : teacherExcels) {
            TeacherUpdateRequest teacher = new TeacherUpdateRequest();
            BeanUtils.copyProperties(e, teacher);
            teacher.setCourseIds(new ArrayList<>());
            for (String s : e.getCourses().split("-")) {
                Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getName, s));
                if (course == null) {
                    response.setError("不存在名为" + s + "的课程");
                }
                teacher.getCourseIds().add(course.getId());
            }
            teacherService.insert(teacher, token);
        }
        response.setCode(20000);
        response.setError("上传完毕");
        return response;
    }
}
