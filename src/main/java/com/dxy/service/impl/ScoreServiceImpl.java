package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.CourseGradeClazzScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.service.ScoreService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public InsertResponse insert(ScoreInsertRequest request) {
        InsertResponse response = new InsertResponse();
        Score score = new Score();
        Integer studentId = request.getStudentId();
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, studentId));
        BeanUtils.copyProperties(request, score);
        score.setClazzId(student.getClazzId());
        response.setCode(scoreMapper.insert(score) == 1 ? 20000 : 20001);
        return response;
    }

    @Override
    public CourseGradeClazzScoreResponse courseClazzScore(String examId, String token) {
        User user = UserUtil.get(token);
        CourseGradeClazzScoreResponse response = new CourseGradeClazzScoreResponse();
        response.setCode(20001);
        response.setClazzMap(new HashMap<>());
        response.setCourseMap(new HashMap<>());
        response.setGradeMap(new HashMap<>());
        response.setMap(new HashMap<>());
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

        examCourses.forEach(
                t -> {
                    Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, t.getCourseId()));
                    response.getCourseMap().put(t.getCourseId(), course);
                    response.getMap().put(t.getCourseId(), new HashMap<>());
                    List<GradeCourse> gradeCourses = gradeCourseMapper.selectList(new LambdaQueryWrapper<GradeCourse>().eq(GradeCourse::getCourseId, course.getId()));
                    gradeCourses.forEach(
                            g -> {
                                Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, g.getGradeId()));
                                response.getGradeMap().put(g.getGradeId(), grade);
                                response.getMap().get(t.getCourseId()).put(grade.getId(), new HashMap<>());
                                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, g.getGradeId()));
                                clazz.forEach(c -> {
                                    response.getClazzMap().put(c.getId(), c);
                                    List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<Score>()
                                            .eq(Score::getExamId, examId)
                                            .eq(Score::getCourseId, t.getCourseId())
                                            .eq(Score::getClazzId, c.getId())
                                    );
                                    response.getMap().get(t.getCourseId()).get(g.getGradeId()).put(c.getId(), scoreList);
                                });
                            }
                    );
                }
        );
        return response;
    }
}
