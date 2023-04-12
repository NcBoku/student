package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.PageGetRequest;
import com.dxy.response.*;
import com.dxy.service.ClazzService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private GradeMapper gradeMapper;


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
                clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, clazzIds)).forEach(
                        c -> {
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

    @Override
    public InsertResponse insert(Clazz clazz, String token) {
        User user = UserUtil.get(token);
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            if (clazzMapper.insert(clazz) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse update(Clazz clazz, String token) {
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            if (clazzMapper.update(clazz, new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, clazz.getId())) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse del(Clazz clazz, String token) {
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            if (clazzMapper.delete(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, clazz.getId())) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public ClazzPageResponse list(PageGetRequest request, String token) {
        Page<Clazz> p = new Page(request.getPage(), request.getSize());
        User user = UserUtil.get(token);
        ClazzPageResponse response = new ClazzPageResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            Page<Clazz> page = clazzMapper.selectPage(p, new LambdaQueryWrapper<Clazz>().orderByDesc(Clazz::getId));
            response.setClazz(new ArrayList<>());
            page.getRecords().forEach(e -> {
                ClazzResponse r = new ClazzResponse();
                BeanUtils.copyProperties(e, r);
                r.setGradeName(gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, e.getGradeId())).getName());
                response.getClazz().add(r);
            });
            response.setTotalPage((int) page.getTotal());
        }
        return response;
    }
}
