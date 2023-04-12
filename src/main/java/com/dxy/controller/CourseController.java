package com.dxy.controller;

import com.dxy.response.ClazzIdsResponse;
import com.dxy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/")
    private ClazzIdsResponse getClazzByGradeId(@RequestBody List<Integer> examIds){
        return null;
    }
}
