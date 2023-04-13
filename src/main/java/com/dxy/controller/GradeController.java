package com.dxy.controller;

import com.dxy.pojo.Grade;
import com.dxy.request.GradeUpdateRequest;
import com.dxy.request.PageGetRequest;
import com.dxy.response.GradePageResponse;
import com.dxy.response.GradesResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grade")
@CrossOrigin
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
    public UpdateResponse delete(@RequestBody List<Grade> grade, @RequestHeader("X-Token") String token) {
        return gradeService.delete(grade, token);
    }

    @PostMapping("/exam/{examId}")
    public GradesResponse getGradesByExamId(@PathVariable("examId")String id,@RequestHeader("X-Token") String token){
        return gradeService.getGradesByExamId(id,token);
    }

    @PostMapping("/list")
    public GradePageResponse list(@RequestBody PageGetRequest request,@RequestHeader("X-Token") String token){
        return gradeService.list(request,token);
    }
}
