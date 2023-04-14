package com.dxy.controller;

import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.CourseGradeClazzScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/score")
@CrossOrigin
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody ScoreInsertRequest request){
        return scoreService.insert(request);
    }

    @PostMapping("/course/clazz/{examId}")
    public CourseGradeClazzScoreResponse courseClazzScore(@PathVariable("examId") String id,@RequestHeader("X-Token") String token){
        return scoreService.courseClazzScore(id,token);
    }
}
