package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.request.ExamGetRequest;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.ExamPageResponse;
import com.dxy.response.ExamScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/exam")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;

    @RequestMapping("/list")
    public ExamPageResponse list(@RequestBody ExamGetRequest request, HttpServletRequest r) {
        return examService.list(new Page<>(request.getPage(), request.getSize()), r.getHeader("X-Token"));
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody ExamInsertRequest request, HttpServletRequest r) {
        return examService.insert(request, r.getHeader("X-Token"));
    }

    @PostMapping("/score")
    public ExamScoreResponse score(@RequestBody ExamScoreRequest request, @RequestHeader("X-Token") String token) {
        return examService.score(request,token);
    }
}
