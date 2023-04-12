package com.dxy.controller;

import com.dxy.pojo.Grade;
import com.dxy.request.GradeUpdateRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody GradeUpdateRequest grade, @RequestHeader("X-Token") String token) {
        return gradeService.insert(grade, token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody GradeUpdateRequest grade, @RequestHeader("X-Token") String token) {
        return gradeService.update(grade, token);
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody Grade grade, @RequestHeader("X-Token") String token) {
        return gradeService.delete(grade, token);
    }
}
