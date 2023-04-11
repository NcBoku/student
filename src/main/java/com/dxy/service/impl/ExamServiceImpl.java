package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.response.*;
import com.dxy.service.ExamService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ExamClazzMapper examClazzMapper;

    @Autowired
    private ExamGradeMapper examGradeMapper;

    @Autowired
    private ExamCourseMapper examCourseMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ScoreMapper scoreMapper;


    @Override
    public ExamPageResponse list(Page<Exam> page, String token) {
        User user = UserUtil.get(token);
        ExamPageResponse response = new ExamPageResponse();
        response.setCode(20001);

        if (user != null) {
            ArrayList<Integer> examIds = new ArrayList<>();
            ArrayList<Integer> courseIds = new ArrayList<>();
            response.setCode(20000);
            response.setTotalPage(0);
            Page<Exam> examPage = null;
            if (user.getType() == 2) {
                Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId()));
                List<ExamGrade> examGrades = examGradeMapper.selectList(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getGradeId, student.getGradeId()));
                examGrades.forEach(e -> {
                    examIds.add(e.getExamId());
                });
                List<ExamClazz> examClazz = examClazzMapper.selectList(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getClazzId, student.getClazzId()));
                examClazz.forEach(e -> {
                    examIds.add(e.getExamId());
                });
                if (examIds.size() == 0) {
                    return response;
                }
                examPage = examMapper.selectPage(page, new LambdaQueryWrapper<Exam>().in(Exam::getId, examIds));
            } else if (user.getType() == 1) {
                Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
                List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
                teacherCourses.forEach(e -> {
                    courseIds.add(e.getCourseId());
                });
                if (courseIds.size() == 0) {
                    return response;
                }
                List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().in(ExamCourse::getCourseId, courseIds));
                examCourses.forEach(
                        e -> {
                            examIds.add(e.getExamId());
                        }
                );
                if (examIds.size() == 0) {
                    return response;
                }
                examPage = examMapper.selectPage(page, new LambdaQueryWrapper<Exam>().in(Exam::getId, examIds));
            } else {
                examPage = examMapper.selectPage(page, new LambdaQueryWrapper<Exam>().orderByDesc(Exam::getTime));
            }
            response.setExams(new ArrayList<>());
            response.setTotalPage((int) examPage.getPages());
            examPage.getRecords().forEach(
                    e -> {
                        ExamResponse examResponse = new ExamResponse();
                        BeanUtils.copyProperties(e, examResponse);
                        if (e.getType() == 0) {
                            ExamGrade examGrade = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, e.getId()));
                            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, examGrade.getGradeId()));
                            examResponse.setGradeName(grade.getName());
                            examResponse.setClazzName("");
                        } else {
                            List<ExamClazz> clazzList = examClazzMapper.selectList(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, e.getId()));
                            ArrayList<Integer> clazzIds = new ArrayList<>();
                            clazzList.forEach(ee -> {
                                clazzIds.add(ee.getClazzId());
                            });
                            StringBuilder clazzStr = new StringBuilder("");
                            StringBuilder cname = new StringBuilder("");
                            examResponse.setGradeName("");
                            clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, clazzIds)).forEach(
                                    ee -> {
                                        clazzStr.append("[" + ee.getName() + "] ");
                                        if (examResponse.getGradeName().equals("")) {
                                            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, ee.getGradeId()));
                                            examResponse.setGradeName(grade.getName());
                                        }
                                    }
                            );
                            examResponse.setClazzName(clazzStr.toString());

                        }
                        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, e.getId()));
                        ArrayList<Integer> examCourseIds = new ArrayList<>();
                        examCourses.forEach(ee -> {
                            examCourseIds.add(ee.getCourseId());
                        });
                        StringBuilder courseStr = new StringBuilder("");
                        courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, examCourseIds)).forEach(
                                ee -> {
                                    courseStr.append("[" + ee.getName() + "]");
                                }
                        );
                        examResponse.setCourseName(courseStr.toString());
                        response.getExams().add(examResponse);
                    }
            );
        }
        return response;
    }

    @Override
    public InsertResponse insert(ExamInsertRequest request, String token) {
        User user = UserUtil.get(token);
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (user != null && user.getType() == 0) {
            response.setCode(20000);
            Exam exam = new Exam();
            exam.setName(request.getName());
            exam.setRemark(request.getRemark());
            exam.setTime(request.getTime());
            exam.setType(request.getType());
            examMapper.insert(exam);

            if (request.getType() == 0) {
                ExamGrade examGrade = new ExamGrade();
                examGrade.setExamId(exam.getId());
                examGrade.setGradeId(request.getGradeId());
                examGradeMapper.insert(examGrade);

            } else if (request.getType() == 1) {
                request.getClazzIds().forEach(
                        id -> {
                            ExamClazz examClazz = new ExamClazz();
                            examClazz.setExamId(exam.getId());
                            examClazz.setClazzId(id);
                            examClazzMapper.insert(examClazz);
                        }
                );
            }
            request.getCourseIds().forEach(
                    id -> {
                        ExamCourse examCourse = new ExamCourse();
                        examCourse.setExamId(exam.getId());
                        examCourse.setCourseId(id);
                        examCourseMapper.insert(examCourse);
                    }
            );
        }
        return response;
    }

    @Override
    public ExamScoreResponse score(ExamScoreRequest request, String token) {
        User user = UserUtil.get(token);
        ExamScoreResponse response = new ExamScoreResponse();
        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, request.getExamId()));
        ArrayList<Integer> list = new ArrayList<>();
        examCourses.forEach(e -> {
            list.add(e.getCourseId());
        });
        List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, list));
        ArrayList<String> tableLabels = new ArrayList<>();
        String headerString = "[ {label:'学号',prop:'number'},{label:'姓名',prop:'name'},";
        for (int i = 0; i < courses.size(); i++) {
            tableLabels.add(courses.get(i).getName());
            headerString += "{label:" + "'" + courses.get(i).getName() + "',prop:'prop" + i + "'}";
            if (i != courses.size() - 1) {
                headerString += ",";
            }
        }

        String data = "[";

        List<Student> students = studentMapper
                .selectList(new LambdaQueryWrapper<Student>()
                        .eq(Student::getClazzId, user.getType() == 2
                                ? studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId())).getClazzId()
                                : request.getClazzId())
                );
        for (int j = 0; j < students.size(); j++) {
            data += "{";
            data += "number:'" + students.get(j).getNumber() + "',";
            data += "name:'" + students.get(j).getName() + "',";
            for (int k = 0; k < courses.size(); k++) {
                Score score = scoreMapper.selectOne(new LambdaQueryWrapper<Score>()
                        .eq(Score::getExamId, request.getExamId())
                        .eq(Score::getStudentId, students.get(j).getId())
                        .eq(Score::getCourseId, courses.get(k).getId())
                );
                data += "prop" + k + ":'" + (score == null ? "成绩未录入" : score.getScore()) + "'";
                if (k != courses.size() - 1) {
                    data += ",";
                }
            }
            data += "}";
            if (j != students.size() - 1) {
                data += ",";
            }
        }

        data += "]";

        headerString += "]";
        response.setCode(20000);
        response.setHeader(headerString);
        response.setData(data);
        return response;
    }
}
