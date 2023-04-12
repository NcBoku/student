package com.dxy.controller;

import com.dxy.response.ClazzIdsResponse;
import com.dxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/clazz")
@CrossOrigin
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @PostMapping("/clazzIds")
    private ClazzIdsResponse getClazzByGradeId(@RequestBody List<Integer> examIds){
        return clazzService.getClazzByGradeId(examIds);
    }

}
