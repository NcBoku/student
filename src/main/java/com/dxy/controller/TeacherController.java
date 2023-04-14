package com.dxy.controller;

import com.dxy.pojo.Student;
import com.dxy.pojo.Teacher;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.request.TeacherUpdateRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.TeacherPageResponse;
import com.dxy.response.TeacherResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController {
    @Autowired
    private TeacherService teacherService;


    @PostMapping("/list")
    public TeacherPageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token) {
        return teacherService.list(request, token);
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody TeacherUpdateRequest request, @RequestHeader("X-Token") String token) {
        return teacherService.insert(request, token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody StudentUpdateRequest request, @RequestHeader("X-Token") String token) {
        return teacherService.update(request, token);
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody List<Teacher> teacher, @RequestHeader("X-Token") String token) {
        return teacherService.delete(teacher, token);
    }

    @PostMapping("/updateAll")
    public UpdateResponse updateAll(@RequestBody TeacherUpdateRequest request, @RequestHeader("X-Token") String token) {
        return teacherService.updateAll(request, token);
    }

    @PostMapping("/info")
    public TeacherResponse info(@RequestHeader("X-Token") String token) {
        return teacherService.info(token);
    }
}
