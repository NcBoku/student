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
    public InsertResponse insert(Clazzroom clazzroom){
        return clazzroomService.insert(clazzroom);
    }

    @PostMapping("/del/{id}")
    public UpdateResponse delete(@PathVariable("id")Integer id){
        return clazzroomService.delete(id);
    }

    @PostMapping("/update")
    public UpdateResponse update(Clazzroom clazzroom){
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

}
