package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.TeacherMapper;
import com.dxy.pojo.Student;
import com.dxy.pojo.Teacher;
import com.dxy.pojo.User;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.StudentPageResponse;
import com.dxy.response.TeacherPageResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.TeacherService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public UpdateResponse update(StudentUpdateRequest request, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        User user = UserUtil.get(token);
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        BeanUtils.copyProperties(request, teacher);
        teacherMapper.update(teacher, new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        return response;
    }

    @Override
    public TeacherPageResponse list(PageGetRequest request, String token) {
        Page<Teacher> teacherPage = new Page<>(request.getPage(), request.getSize());
        TeacherPageResponse response = new TeacherPageResponse();
        response.setTeachers(new ArrayList<>());
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            Page<Teacher> page = teacherMapper.selectPage(teacherPage, new LambdaQueryWrapper<Teacher>().orderByDesc(Teacher::getId));
            response.setCode(20000);
            response.setTotal((int) page.getTotal());
            page.getRecords().forEach(
                    e -> {
                        response.getTeachers().add(e);
                    }
            );
        }
        return response;
    }

    @Override
    public InsertResponse insert(Teacher teacher, String token) {
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            if (teacherMapper.insert(teacher) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Teacher> teachers, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            ArrayList<Integer> ids = new ArrayList<>();
            teachers.forEach(e -> {
                ids.add(e.getId());
            });
            if (teacherMapper.delete(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, ids)) == teachers.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }
}
