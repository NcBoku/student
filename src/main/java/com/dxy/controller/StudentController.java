package com.dxy.controller;

import com.dxy.pojo.Student;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.*;
import com.dxy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list")
    public StudentPageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token){
        return studentService.list(request,token);
    }

    @PostMapping("/insert")
    public UpdateResponse insert(@RequestBody Student student,@RequestHeader("X-Token") String token){
        return studentService.insert(student,token);
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody List<Student> students,@RequestHeader("X-Token") String token){
        return studentService.delete(students,token);
    }

    @PostMapping("/updateAll")
    public UpdateResponse updateAll(@RequestBody Student student,@RequestHeader("X-Token") String token){
        return studentService.updateAll(student,token);
    }
}
