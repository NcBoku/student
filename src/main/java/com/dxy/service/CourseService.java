package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Course;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.CoursePageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface CourseService extends IService<Course> {
    CoursePageResponse list(PageGetRequest request, String token);

    InsertResponse insert(Course course, String token);

    UpdateResponse update(Course course, String token);

    UpdateResponse delete(List<Course> course, String token);

    CoursePageResponse getCourseByExamId(@PathVariable("id") Integer id, @RequestHeader("X-Token") String token);
}
