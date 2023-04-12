package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.ExamClazzMapper;
import com.dxy.mapper.ExamGradeMapper;
import com.dxy.mapper.ExamMapper;
import com.dxy.pojo.Clazz;
import com.dxy.pojo.Exam;
import com.dxy.pojo.ExamClazz;
import com.dxy.pojo.ExamGrade;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamGradeMapper examGradeMapper;

    @Autowired
    private ExamClazzMapper examClazzMapper;


    @Override
    public ClazzIdsResponse getClazzByGradeId(List<Integer> ids) {
        HashMap<Integer, List<Clazz>> map = new HashMap<>();
        List<Exam> exams = examMapper.selectList(new LambdaQueryWrapper<Exam>().in(Exam::getId, ids));

        exams.forEach(e -> {
            ArrayList<Clazz> list = new ArrayList<>();
            map.put(e.getId(), list);
            if (e.getType() == 0) {
                ExamGrade examGrade = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, e.getId()));
                if (examGrade == null) {
                    return;
                }

                clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, examGrade.getGradeId())).forEach(
                        c -> {
                            list.add(c);
                        }
                );
                map.put(e.getId(), list);
            } else if (e.getType() == 1) {
                ArrayList<Integer> clazzIds = new ArrayList<>();
                List<ExamClazz> examClazz = examClazzMapper.selectList(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, e.getId()));
                if (examClazz.size() == 0) {
                    return;
                }
                examClazz.forEach(
                        ec -> {
                            clazzIds.add(ec.getClazzId());
                        }
                );
                clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId,clazzIds)).forEach(
                        c->{
                            list.add(c);
                        }
                );
            }
        });
        ClazzIdsResponse response = new ClazzIdsResponse();
        response.setCode(20000);
        response.setMap(map);
        return response;
    }
}
