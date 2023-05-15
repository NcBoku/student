package com.dxy.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.CourseGradeClazzScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.ScoreExcel;
import com.dxy.response.UpdateResponse;
import com.dxy.service.ScoreService;
import com.dxy.util.ExcelUtil;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamCourseMapper examCourseMapper;

    @Autowired
    private ExamClazzroomMapper examClazzroomMapper;

    @Autowired
    private ClazzroomMapper clazzroomMapper;

    @Override
    @Transactional
    public InsertResponse insert(ScoreInsertRequest request) {
        InsertResponse response = new InsertResponse();
        Score score = new Score();
        Integer studentId = request.getStudentId();
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, studentId));
        BeanUtils.copyProperties(request, score);
        if (score.getScore() != null && score.getPscore() != null) {
            double s = score.getScore() * 0.7 + score.getPscore() * 0.3;
            if (s >= 90) {
                score.setLevel("A");
            } else if (s >= 80) {
                score.setLevel("B");
            } else if (s >= 70) {
                score.setLevel("C");
            } else if (s >= 60) {
                score.setLevel("D");
            } else {
                score.setLevel("E");
            }
        }
        score.setClazzId(student.getClazzId());
        response.setCode(scoreMapper.insert(score) == 1 ? 20000 : 20001);
        return response;
    }

    @Override
    @Transactional
    public CourseGradeClazzScoreResponse courseClazzScore(String examId, String token) {
        User user = UserUtil.get(token);
        CourseGradeClazzScoreResponse response = new CourseGradeClazzScoreResponse();
        response.setCode(20000);
        response.setClazzMap(new ArrayList<>());
        response.setCourseMap(new ArrayList<>());
        response.setGradeMap(new ArrayList<>());
        response.setMap(new HashMap<>());
        response.setStudents(new ArrayList<>());
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
        ArrayList<Integer> cids = new ArrayList<>();
        teacherCourses.forEach(e -> {
            cids.add(e.getCourseId());
        });
        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>()
                .eq(ExamCourse::getExamId, examId)
                .in(ExamCourse::getCourseId, cids)
        );
        ArrayList<Integer> sids = new ArrayList<>();
        examCourses.forEach(
                t -> {
                    Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, t.getCourseId()));
                    response.getCourseMap().add(course);
                    response.getMap().put(t.getCourseId(), new HashMap<>());

                    List<GradeCourse> gradeCourses = gradeCourseMapper.selectList(new LambdaQueryWrapper<GradeCourse>().eq(GradeCourse::getCourseId, course.getId()));
                    gradeCourses.forEach(
                            g -> {
                                Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, g.getGradeId()));
                                if (grade == null) {
                                    return;
                                }
                                response.getGradeMap().add(grade);
                                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, g.getGradeId()));
                                clazz.forEach(c -> {
                                    if (!response.getClazzMap().contains(c))
                                        response.getClazzMap().add(c);
                                    List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<Score>()
                                            .eq(Score::getExamId, examId)
                                            .eq(Score::getCourseId, t.getCourseId())
                                            .eq(Score::getClazzId, c.getId())
                                    );

                                    scoreList.forEach(s -> {
                                        sids.add(s.getStudentId());
                                    });
                                    if (sids.size() != 0) {
                                        List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getId, sids));
                                        response.setStudents(students);
                                        response.getMap().get(t.getCourseId()).put(c.getId(), scoreList);
                                    } else {
                                        response.getClazzMap().remove(response.getClazzMap().size() - 1);
                                        response.getMap().get(t.getCourseId()).remove(g.getGradeId());
                                    }

                                });
                            }
                    );
                }
        );
        return response;
    }

    @Override
    @Transactional
    public UpdateResponse update(List<Score> scoreList, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 1) {
            response.setCode(20000);

            scoreList.forEach(e -> {
                if (e.getScore() != null && e.getPscore() != null) {
                    double s = e.getScore() * 0.7 + e.getPscore() * 0.3;
                    if (s >= 90) {
                        e.setLevel("A");
                    } else if (s >= 80) {
                        e.setLevel("B");
                    } else if (s >= 70) {
                        e.setLevel("C");
                    } else if (s >= 60) {
                        e.setLevel("D");
                    } else {
                        e.setLevel("E");
                    }
                }

                scoreMapper.update(e, new LambdaQueryWrapper<Score>().eq(Score::getId, e.getId()));
            });
        }
        return response;
    }

    @Override
    @Transactional
    public void getScoreExcelData(HttpServletResponse response) {
        System.out.println("service");
        HashMap<Exam, List<ScoreExcel>> data = new HashMap<>();
        List<Exam> exams = examMapper.selectList(null);
        for (Exam exam : exams) {
            List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<Score>().eq(Score::getExamId, exam.getId()));
            data.put(exam, new ArrayList<>());
            for (Score score : scoreList) {
                ScoreExcel scoreExcel = new ScoreExcel();
                BeanUtils.copyProperties(score, scoreExcel);
                Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, score.getStudentId()));
                scoreExcel.setStudent(student.getName());
                scoreExcel.setNumber(student.getNumber());
                List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>()
                        .eq(ExamClazzroom::getExamId, exam.getId()));
                examClazzrooms.forEach(
                        e -> {
                            String students = e.getStudents();
                            for (String s : students.split(",")) {
                                if (Integer.parseInt(s) == student.getId()) {
                                    Clazzroom clazzroom = clazzroomMapper.selectOne(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, e.getClazzroomId()));
                                    scoreExcel.setName(clazzroom.getLocation() + " " + clazzroom.getName());
                                    StringBuilder sb = new StringBuilder("");
                                    for (String s1 : e.getTeachers().split(",")) {
                                        int id = Integer.parseInt(s1);
                                        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId, id));
                                        sb.append(teacher.getName()+" ");
                                    }
                                    scoreExcel.setTeachers(sb.toString());
                                    return;
                                }
                            }
                        }
                );
                scoreExcel.setScore(score.getScore());
                scoreExcel.setPscore(score.getPscore());
                scoreExcel.setStart(exam.getTime());
                scoreExcel.setEnd(exam.getEnd());
                scoreExcel.setClazz(clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, student.getClazzId())).getName());
                scoreExcel.setCourse(courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, score.getCourseId())).getName());
                data.get(exam).add(scoreExcel);
            }
        }
        ExcelUtil.exportExamAndScoreExcel(data, response);
    }

    @Override
    @Transactional
    public void getScoreExcelData(List<Integer> ids,HttpServletResponse response) {
        System.out.println("service");
        HashMap<Exam, List<ScoreExcel>> data = new HashMap<>();
        List<Exam> exams = examMapper.selectList(new LambdaQueryWrapper<Exam>().in(Exam::getId,ids));
        for (Exam exam : exams) {
            List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<Score>().eq(Score::getExamId, exam.getId()));
            data.put(exam, new ArrayList<>());
            for (Score score : scoreList) {
                ScoreExcel scoreExcel = new ScoreExcel();
                BeanUtils.copyProperties(score, scoreExcel);
                Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, score.getStudentId()));
                scoreExcel.setStudent(student.getName());
                scoreExcel.setNumber(student.getNumber());
                List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>()
                        .eq(ExamClazzroom::getExamId, exam.getId()));
                examClazzrooms.forEach(
                        e -> {
                            String students = e.getStudents();
                            for (String s : students.split(",")) {
                                if (Integer.parseInt(s) == student.getId()) {
                                    Clazzroom clazzroom = clazzroomMapper.selectOne(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, e.getClazzroomId()));
                                    scoreExcel.setName(clazzroom.getLocation() + " " + clazzroom.getName());
                                    StringBuilder sb = new StringBuilder("");
                                    for (String s1 : e.getTeachers().split(",")) {
                                        int id = Integer.parseInt(s1);
                                        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId, id));
                                        sb.append(teacher.getName()+" ");
                                    }
                                    scoreExcel.setTeachers(sb.toString());
                                    return;
                                }
                            }
                        }
                );
                scoreExcel.setScore(score.getScore());
                scoreExcel.setPscore(score.getPscore());
                scoreExcel.setStart(exam.getTime());
                scoreExcel.setEnd(exam.getEnd());
                scoreExcel.setClazz(clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, student.getClazzId())).getName());
                scoreExcel.setCourse(courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, score.getCourseId())).getName());
                data.get(exam).add(scoreExcel);
            }
        }
        ExcelUtil.exportExamAndScoreExcel(data, response);
    }
}
