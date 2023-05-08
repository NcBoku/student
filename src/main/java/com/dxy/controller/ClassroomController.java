package com.dxy.controller;

import com.dxy.response.InsertResponse;
import com.dxy.service.ClazzroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classroom")
@CrossOrigin
public class ClassroomController {
    @Autowired
    private ClazzroomService clazzroomService;

    @PostMapping("/insert")
    public InsertResponse insert(){
        return null;
    }

    @PostMapping("/list")
    public List<>

}
