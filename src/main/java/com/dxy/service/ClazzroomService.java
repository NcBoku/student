package com.dxy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.mapper.ClazzroomMapper;
import com.dxy.pojo.Clazz;
import com.dxy.pojo.Clazzroom;
import com.dxy.pojo.Teacher;
import com.dxy.request.PageGetRequest;
import com.dxy.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
public interface ClazzroomService  extends IService<Clazzroom> {
    void updateInfo();
    List<Clazzroom> getRestClazzroom(Date start,Date end);
    List<Clazz> getNotRestClazz(Date start,Date end);
    List<Teacher> getNotRestTeacher(Date start, Date end );
    ClazzroomResponse getInfo(Integer id);
    ClazzroomPageResponse getList(PageGetRequest request);
    InsertResponse insert(Clazzroom clazzroom);
    UpdateResponse update(Clazzroom clazzroom);
    UpdateResponse delete(List<Integer>  id);
    UserExamClazzroomPageResponse student(PageGetRequest request,Integer id);
    UserExamClazzroomPageResponse teacher(PageGetRequest request,Integer id);
}
