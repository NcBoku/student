package com.dxy.controller;

import com.dxy.pojo.Clazz;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.ClazzPageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
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
    private ClazzIdsResponse getClazzByGradeId(@RequestBody List<Integer> examIds) {
        return clazzService.getClazzByGradeId(examIds);
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody Clazz clazz, @RequestHeader("X-Token") String token) {
        return clazzService.insert(clazz, token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody Clazz clazz, @RequestHeader("X-Token") String token) {
        return clazzService.update(clazz, token);
    }

    @PostMapping("/del")
    public UpdateResponse del(@RequestBody List<Clazz> clazz, @RequestHeader("X-Token") String token) {
        return clazzService.del(clazz, token);
    }

    @PostMapping("/list")
    public ClazzPageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token){
        return clazzService.list(request,token);
    }

}
