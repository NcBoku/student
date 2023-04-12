package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ScoreMapper;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Score;
import com.dxy.pojo.Student;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.InsertResponse;
import com.dxy.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

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
}
