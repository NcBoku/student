package com.dxy.controller;

import com.dxy.pojo.Course;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.CoursePageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/list")
    public CoursePageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token){
        return courseService.list(request,token);
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody Course course, @RequestHeader("X-Token") String token){
        return courseService.insert(course,token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody Course course, @RequestHeader("X-Token") String token){
        return courseService.update(course,token);
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody List<Course> course, @RequestHeader("X-Token") String token){
        return courseService.delete(course,token);
    }
}
