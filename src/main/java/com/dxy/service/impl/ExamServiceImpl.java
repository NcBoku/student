package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.response.*;
import com.dxy.service.ClazzroomService;
import com.dxy.service.ExamService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private ClazzroomService clazzroomService;

    @Autowired
    private ExamClazzroomMapper examClazzroomMapper;

    @Autowired
    private ClazzroomMapper clazzroomMapper;


    @Override
    @Transactional
    public ExamPageResponse list(Page<Exam> page, String token, String keyword) {
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
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.and(
                            o -> o.like(Exam::getId, keyword)
                                    .or()
                                    .like(Exam::getName, keyword)
                                    .or()
                                    .like(Exam::getRemark, keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.in(Exam::getId, examIds));
            } else if (user.getType() == 1) {
                Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
                List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
                teacherCourses.forEach(e -> {
                    courseIds.add(e.getCourseId());
                });
                response.setExams(new ArrayList<>());
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
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.and(
                            o -> o.like(Exam::getId, keyword)
                                    .or()
                                    .like(Exam::getName, keyword)
                                    .or()
                                    .like(Exam::getRemark, keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.in(Exam::getId, examIds));
            } else {
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.and(
                            o -> o.like(Exam::getId, keyword)
                                    .or()
                                    .like(Exam::getName, keyword)
                                    .or()
                                    .like(Exam::getRemark, keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.orderByDesc(Exam::getTime));
            }


            response.setExams(new ArrayList<>());
            response.setTotalPage((int) examPage.getTotal());
            examPage.getRecords().forEach(
                    e -> {
                        ExamResponse examResponse = new ExamResponse();
                        response.getExams().add(examResponse);
                        BeanUtils.copyProperties(e, examResponse);
                        if (e.getType() == 0) {
                            ExamGrade examGrade = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, e.getId()));
                            if (examGrade == null) {
                                return;
                            }
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
                            examResponse.setGradeName("");
                            if (clazzIds.size() == 0) {
                                return;
                            }
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
                                    courseStr.append(ee.getName() + ",");
                                }
                        );
                        courseStr.deleteCharAt(courseStr.length() - 1);
                        examResponse.setCourseName(courseStr.toString());

                        StringBuilder clazzRoom = new StringBuilder("");
                        StringBuilder teacherNames = new StringBuilder("");
                        List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getExamId, e.getId()));
                        ArrayList<Integer> clazzRoomIds = new ArrayList<>();
                        ArrayList<Integer> teacherIds = new ArrayList<>();
                        examClazzrooms.forEach(ee -> {
                            clazzRoomIds.add(ee.getClazzroomId());
                            for (String s : ee.getTeachers().split(",")) {
                                teacherIds.add(Integer.parseInt(s));
                            }
                        });
                        clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().in(Clazzroom::getId, clazzRoomIds)).forEach(ee -> {
                            clazzRoom.append(ee.getName() + ",");
                        });
                        clazzRoom.deleteCharAt(clazzRoom.length() - 1);
                        examResponse.setClazzroomName(clazzRoom.toString());

                        teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, teacherIds)).forEach(ee -> {
                            teacherNames.append(ee.getName() + ",");
                        });
                        teacherNames.deleteCharAt(teacherNames.length() - 1);
                        examResponse.setTeacherNames(teacherNames.toString());
                    }
            );
        }
        return response;
    }

    @Override
    public ExamPageResponse plan(Page<Exam> page, String token, String keyword) {
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
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.ge(Exam::getTime, new Date(System.currentTimeMillis()))
                            .and(
                                    o -> o.like(Exam::getId, keyword)
                                            .or()
                                            .like(Exam::getName, keyword)
                                            .or()
                                            .like(Exam::getRemark, keyword)
                            );

                }
                examPage = examMapper.selectPage(page, wrapper.in(Exam::getId, examIds));
            } else if (user.getType() == 1) {
                Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
                List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
                teacherCourses.forEach(e -> {
                    courseIds.add(e.getCourseId());
                });
                response.setExams(new ArrayList<>());
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
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.and(
                            o -> o.like(Exam::getId, keyword)
                                    .or()
                                    .like(Exam::getName, keyword)
                                    .or()
                                    .like(Exam::getRemark, keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.in(Exam::getId, examIds));
            } else {
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword != null && !keyword.equals("")) {
                    wrapper.and(
                            o -> o.like(Exam::getId, keyword)
                                    .or()
                                    .like(Exam::getName, keyword)
                                    .or()
                                    .like(Exam::getRemark, keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.orderByDesc(Exam::getTime));
            }


            response.setExams(new ArrayList<>());
            response.setTotalPage((int) examPage.getTotal());
            examPage.getRecords().forEach(
                    e -> {
                        ExamResponse examResponse = new ExamResponse();
                        response.getExams().add(examResponse);
                        BeanUtils.copyProperties(e, examResponse);
                        if (e.getType() == 0) {
                            ExamGrade examGrade = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, e.getId()));
                            if (examGrade == null) {
                                return;
                            }
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
                            examResponse.setGradeName("");
                            if (clazzIds.size() == 0) {
                                return;
                            }
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

                        StringBuilder clazzRoom = new StringBuilder("");
                        StringBuilder teacherNames = new StringBuilder("");
                        List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getExamId, e.getId()));
                        ArrayList<Integer> clazzRoomIds = new ArrayList<>();
                        ArrayList<Integer> teacherIds = new ArrayList<>();
                        examClazzrooms.forEach(ee -> {
                            clazzRoomIds.add(ee.getClazzroomId());
                            for (String s : ee.getTeachers().split(",")) {
                                teacherIds.add(Integer.parseInt(s));
                            }
                        });
                        clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().in(Clazzroom::getId, clazzRoomIds)).forEach(ee -> {
                            clazzRoom.append(ee.getName() + ",");
                        });
                        clazzRoom.deleteCharAt(clazzRoom.length() - 1);
                        examResponse.setClazzroomName(clazzRoom.toString());

                        teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, teacherIds)).forEach(ee -> {
                            teacherNames.append(ee.getName() + ",");
                        });
                        teacherNames.deleteCharAt(teacherNames.length() - 1);
                        examResponse.setTeacherNames(teacherNames.toString());
                    }
            );
        }
        return response;
    }

    @Override
    @Transactional
    public InsertResponse insert(ExamInsertRequest request, String token) {
        System.out.println("test" + request.getTime() + " " + request.getEnd());
        User user = UserUtil.get(token);
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (request.getType() != 0) {
            Integer id = request.getClazzIds().get(0);
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, id));
            request.setGradeId(clazz.getGradeId());
        }
        for (Integer id : request.getCourseIds()) {
            if (gradeCourseMapper.selectOne(new LambdaQueryWrapper<GradeCourse>()
                    .eq(GradeCourse::getCourseId, id)
                    .eq(GradeCourse::getGradeId, request.getGradeId())) == null) {
                return response;
            }
        }

        if (true) {
            response.setCode(20000);
            response.setError(null);
            int count = 0;
            List<Student> ss = new ArrayList<>();
            List<Clazz> cc = new ArrayList<>();
            if (request.getType() == 0) {
                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, request.getGradeId()));
                cc = clazz;
                ArrayList<Integer> clazzIds = new ArrayList<>();
                clazz.forEach(e -> {
                    clazzIds.add(e.getId());
                });
                List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getClazzId, clazzIds));
                count = students.size();
                ss = students;
            } else if (request.getType() == 1) {
                List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getClazzId, request.getClazzIds()));
                ss = students;
                cc = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, request.getClazzIds()));
                count = students.size();
            }
            List<Clazzroom> restClazzroom = clazzroomService.getRestClazzroom(request.getTime(), request.getEnd());
            System.out.println("rest clazz room"+restClazzroom);
            List<Clazz> restClazz = clazzroomService.getNotRestClazz(request.getTime(), request.getEnd());
            System.out.println("not rest clazz"+restClazz);
            List<Teacher> notRestTeacher = clazzroomService.getNotRestTeacher(request.getTime(), request.getEnd());
            System.out.println("not rest teachers"+notRestTeacher);
            ArrayList<Integer> tids = new ArrayList<>();

            notRestTeacher.forEach(e -> {
                tids.add(e.getId());
            });

            List<Teacher> restTeachers = tids.size() == 0 ? teacherMapper.selectList(null) :
                    teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().notIn(Teacher::getId, tids));

            if (restTeachers.size() == 0) {
                response.setError("该时间段无空闲的监考教师");
                return response;
            }

            for (Clazz c1 : cc) {
                for (Clazz c2 : restClazz) {
                    if (c1.getId().equals(c2.getId())) {
                        response.setError(c1.getName() + "在此时间段存在其他考试!!!");
                        return response;
                    }
                }
            }

            if (restClazzroom == null) {
                response.setError("该时间段没有空闲的教室");
                return response;
            }

            int max = 0, l = 0, t;
            for (int i = 0; i < restClazzroom.size(); i++) {
                max += restClazzroom.get(i).getCount();
                if (max >= count) {
                    l = i;
                    break;
                }
            }


            t = l + 1;
            if (t > restTeachers.size()) {
                response.setError("该时间段无足够的监考教师");
                return response;
            }

            if (max < count) {
                response.setError("该时间段没有足够的教室");
                return response;
            }

            Exam exam = new Exam();
            exam.setName(request.getName());
            exam.setRemark(request.getRemark());
            exam.setTime(request.getTime());
            exam.setEnd(request.getEnd());
            exam.setType(request.getType());
            examMapper.insert(exam);

            int k = 0;
            for (int i = 0; i <= l; i++) {
                ExamClazzroom examClazzroom = new ExamClazzroom();
                examClazzroom.setStart(request.getTime());
                examClazzroom.setEnd(request.getEnd());
                examClazzroom.setExamId(exam.getId());
                examClazzroom.setClazzroomId(restClazzroom.get(i).getId());
                examClazzroom.setIsDeleted(false);
                if (restTeachers.size() >= t * 2) {
                    examClazzroom.setTeachers(restTeachers.get(i * 2).getId() + "," + restTeachers.get(i * 2 + 1).getId());
                } else {
                    examClazzroom.setTeachers(restTeachers.get(i).getId() + "");
                }
                String s = "";
                for (int j = 0; j < restClazzroom.get(i).getCount() && k < count; j++) {
                    s += ss.get(k++).getId();
                    if (j != restClazzroom.get(i).getCount() - 1) {
                        s+=",";
                    }
                }
                examClazzroom.setStudents(s);
                examClazzroomMapper.insert(examClazzroom);
            }

            ss.forEach(e -> {
                request.getCourseIds().forEach(c -> {
                    Score score = new Score();
                    score.setClazzId(e.getClazzId());
                    score.setScore(null);
                    score.setExamId(exam.getId());
                    score.setStudentId(e.getId());
                    score.setCourseId(c);
                    scoreMapper.insert(score);
                });
            });
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
    @Transactional
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
            headerString += "{label:" + "'" + courses.get(i).getName() + "平时分" + "',prop:'p" + i + "'}";
            headerString += ",{label:" + "'" + courses.get(i).getName() + "考试分" + "',prop:'k" + i + "'}";
            headerString += ",{label:" + "'" + "评价" + "',prop:'l" + i + "'}";
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
                data += "p" + k + ":'" + ((score == null || score.getPscore() == null) ? "-" : score.getPscore()) + "'";
                data += ",k" + k + ":'" + ((score == null || score.getScore() == null) ? "-" : score.getScore()) + "'";
                data += ",l" + k + ":'" + ((score == null || score.getLevel() == null) ? "-" : score.getLevel()) + "'";
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

    @Override
    @Transactional
    public UpdateResponse update(ExamInsertRequest request, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (request.getType() != 0) {
            Integer id = request.getClazzIds().get(0);
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, id));
            request.setGradeId(clazz.getGradeId());
        }
        for (Integer id : request.getCourseIds()) {
            if (gradeCourseMapper.selectOne(new LambdaQueryWrapper<GradeCourse>()
                    .eq(GradeCourse::getCourseId, id)
                    .eq(GradeCourse::getGradeId, request.getGradeId())) == null) {
                return response;
            }
        }
        User user = UserUtil.get(token);
        if (user != null && user.getType() == 0) {
            response.setCode(20000);
            examClazzMapper.delete(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, request.getId()));
            examGradeMapper.delete(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, request.getId()));
            examCourseMapper.delete(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, request.getId()));

            Exam exam = new Exam();
            BeanUtils.copyProperties(request, exam);
            examMapper.update(exam, new LambdaQueryWrapper<Exam>().eq(Exam::getId, exam.getId()));

            List<Student> students = new ArrayList<>();

            if (request.getType() == 0) {
                ExamGrade examGrade = new ExamGrade();
                examGrade.setExamId(request.getId());
                examGrade.setGradeId(request.getGradeId());
                examGradeMapper.insert(examGrade);
                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, request.getGradeId()));
                clazz.forEach(c -> {
                    List<Student> list = studentMapper.selectList(new LambdaQueryWrapper<Student>().eq(Student::getClazzId, c.getId()));
                    students.addAll(list);
                });
            } else if (request.getType() == 1) {
                request.getClazzIds().forEach(
                        id -> {
                            ExamClazz examClazz = new ExamClazz();
                            examClazz.setExamId(request.getId());
                            examClazz.setClazzId(id);
                            examClazzMapper.insert(examClazz);
                            List<Student> list = studentMapper.selectList(new LambdaQueryWrapper<Student>().eq(Student::getClazzId, id));
                            students.addAll(list);
                        }
                );
            }
            request.getCourseIds().forEach(
                    id -> {
                        ExamCourse examCourse = new ExamCourse();
                        examCourse.setExamId(request.getId());
                        examCourse.setCourseId(id);
                        examCourseMapper.insert(examCourse);
                        students.forEach(e -> {
                            Score score = new Score();
                            score.setExamId(request.getId());
                            score.setClazzId(e.getClazzId());
                            score.setStudentId(e.getId());
                            score.setCourseId(id);
                            if (scoreMapper.selectOne(new LambdaQueryWrapper<Score>()
                                    .eq(Score::getExamId, score.getExamId())
                                    .eq(Score::getCourseId, score.getCourseId())
                                    .eq(Score::getStudentId, score.getStudentId())
                            ) == null) {
                                scoreMapper.insert(score);
                            }

                        });
                    }
            );

        }
        return response;
    }

    @Override
    @Transactional
    public UpdateResponse delete(List<Exam> id, String token) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Exam exam : id) {
            ids.add(exam.getId());
        }
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        if (user != null && user.getType() == 0) {
            examClazzroomMapper.delete(new LambdaQueryWrapper<ExamClazzroom>().in(ExamClazzroom::getExamId, ids));
            scoreMapper.delete(new LambdaQueryWrapper<Score>().in(Score::getExamId, ids));
            examGradeMapper.delete(new LambdaQueryWrapper<ExamGrade>().in(ExamGrade::getExamId, ids));
            examCourseMapper.delete(new LambdaQueryWrapper<ExamCourse>().in(ExamCourse::getExamId, ids));
            examClazzMapper.delete(new LambdaQueryWrapper<ExamClazz>().in(ExamClazz::getExamId, ids));
            examMapper.delete(new LambdaQueryWrapper<Exam>().in(Exam::getId, ids));
            response.setCode(20000);
        }
        return response;
    }
}