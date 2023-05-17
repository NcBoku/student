package com.dxy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.CourseMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.mapper.TeacherCourseMapper;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @GetMapping("/score")
    public void score(HttpServletResponse response) {
        System.out.println("开始");
        scoreService.getScoreExcelData(response);

    }

    @GetMapping("/score/{ids}")
    public void score(@PathVariable("ids") String ids, HttpServletResponse response) {
        System.out.println("开始");
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.parseInt(s));
        }
        scoreService.getScoreExcelData(integers, response);

    }

    @PostMapping("/upload/student/{token}")
    @Transactional(propagation = Propagation.NESTED)
    public UpdateResponse student(@RequestParam("file") MultipartFile multipartFile, @PathVariable("token") String token) {
        UpdateResponse response = new UpdateResponse();
        ArrayList<Student> students = new ArrayList<>();
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
            students.add(student);
        }
        students.forEach(e -> {
            studentService.insert(e, token);
        });
        response.setCode(20000);
        response.setError("上传完毕");
        return response;
    }

    @Transactional(propagation = Propagation.NESTED)
    @PostMapping("/upload/teacher/{token}")
    public UpdateResponse teacher(@RequestParam("file") MultipartFile multipartFile, @PathVariable("token") String token) {
        UpdateResponse response = new UpdateResponse();
        ArrayList<TeacherUpdateRequest> teachers = new ArrayList<>();
        List<TeacherExcel> teacherExcels = ExcelUtil.importTeacherExcel(multipartFile);
        for (TeacherExcel e : teacherExcels) {
            TeacherUpdateRequest teacher = new TeacherUpdateRequest();
            BeanUtils.copyProperties(e, teacher);
            teacher.setCourseIds(new ArrayList<>());
            for (String s : e.getCourses().split("-")) {
                Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getName, s));
                if (course == null) {
                    response.setError("不存在名为" + s + "的课程");
                    return response;
                }
                teacher.getCourseIds().add(course.getId());
            }
            teachers.add(teacher);
        }
        teachers.forEach(e -> {
            teacherService.insert(e, token);
        });
        response.setCode(20000);
        response.setError("上传完毕");
        return response;
    }

    @GetMapping("/students")
    public void students(HttpServletResponse response) {
        List<Student> list = studentService.list(null);
        HashMap<Clazz, List<StudentExcel>> map = new HashMap<>();
        for (Student student : list) {
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, student.getClazzId()));
            if (!map.containsKey(clazz)) {
                map.put(clazz, new ArrayList<>());
            }
            StudentExcel studentExcel = new StudentExcel();
            BeanUtils.copyProperties(student, studentExcel);
            studentExcel.setClazz(clazz.getName());
            studentExcel.setGrade(gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, clazz.getGradeId())).getName());
            map.get(clazz).add(studentExcel);
        }
        ExcelUtil.exportStudentsExcel(map, response);
    }

    @GetMapping("/teachers")
    public void teachers(HttpServletResponse response) {
        List<Teacher> list = teacherService.list(null);
        HashMap<Clazz, List<TeacherExcel>> map = new HashMap<>();
        Clazz clazz = new Clazz();
        clazz.setName("教师表");
        map.put(clazz, new ArrayList<>());
        for (Teacher teacher : list) {
            TeacherExcel teacherExcel = new TeacherExcel();
            BeanUtils.copyProperties(teacher, teacherExcel);
            List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
            String s = "";
            ArrayList<Integer> ids = new ArrayList<>();
            teacherCourses.forEach(e -> {
                ids.add(e.getCourseId());
            });
            List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, ids));
            for (int i = 0; i < courses.size(); i++) {
                s += courses.get(i).getName();
                if (i != courses.size() - 1) {
                    s += "-";
                }
            }
            teacherExcel.setCourses(s);
            map.get(clazz).add(teacherExcel);
        }
        ExcelUtil.exportTeachersExcel(map, response);
    }
}
