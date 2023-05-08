package com.dxy.controller;

import com.dxy.response.ClazzroomResponse;
import com.dxy.response.InsertResponse;
import com.dxy.service.ClazzroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list/{id}")
    public ClazzroomResponse list(@PathVariable("id") Integer id){
        return clazzroomService.getInfo(id);
    }

}
