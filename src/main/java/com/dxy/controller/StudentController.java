package com.dxy.controller;

import com.dxy.pojo.Student;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.StudentResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/info")
    public StudentResponse info(@RequestHeader("X-Token") String token) {
        return studentService.info(token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody StudentUpdateRequest request, @RequestHeader("X-Token") String token) {
        return studentService.update(request, token);
    }
}
