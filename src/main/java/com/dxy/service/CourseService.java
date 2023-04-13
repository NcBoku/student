package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Course;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.CoursePageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;

import java.util.List;

public interface CourseService extends IService<Course> {
    CoursePageResponse list(PageGetRequest request, String token);
     InsertResponse insert(Course course,String token);
     UpdateResponse update(Course course,String token);
    UpdateResponse delete(List<Course> course, String token);
}
