package com.dxy.controller;

import com.dxy.pojo.Clazz;
import com.dxy.pojo.Clazzroom;
import com.dxy.request.PageGetRequest;
import com.dxy.response.*;
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
    public InsertResponse insert(@RequestBody Clazzroom clazzroom){
        return clazzroomService.insert(clazzroom);
    }

    @PostMapping("/del")
    public UpdateResponse delete(@RequestBody List<Integer>  id){
        return clazzroomService.delete(id);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody Clazzroom clazzroom){
        return clazzroomService.update(clazzroom);
    }

    @PostMapping("/list")
    public ClazzroomPageResponse list(@RequestBody PageGetRequest request){
        return clazzroomService.getList(request);
    }

    @PostMapping("/list/{id}")
    public ClazzroomResponse info(@PathVariable("id") Integer id){
        return clazzroomService.getInfo(id);
    }

    @PostMapping("/student/{id}")
    public UserExamClazzroomPageResponse student(@RequestBody PageGetRequest request,@PathVariable("id") Integer id){
        return clazzroomService.student(request,id);
    }

    @PostMapping("/teacher/{id}")
    public UserExamClazzroomPageResponse teacher(@RequestBody PageGetRequest request,@PathVariable("id") Integer id){
        return clazzroomService.teacher(request,id);
    }

}
