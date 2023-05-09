package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.pojo.Exam;
import com.dxy.request.*;
import com.dxy.response.ExamPageResponse;
import com.dxy.response.ExamScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/exam")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;

    //  从这个里面查找考试安排和监考安排
    @RequestMapping("/list")
    public ExamPageResponse list(@RequestBody PageGetRequest request, HttpServletRequest r) {
        return examService.list(new Page<>(request.getPage(), request.getSize()), r.getHeader("X-Token"), request.getKeyword());
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody ExamInsertRequest request, HttpServletRequest r) {
        return examService.insert(request, r.getHeader("X-Token"));
    }

    @PostMapping("/score")
    public ExamScoreResponse score(@RequestBody ExamScoreRequest request, @RequestHeader("X-Token") String token) {
        return examService.score(request, token);
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody ExamInsertRequest request, @RequestHeader("X-Token") String token) {
        return examService.update(request, token);
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody List<Exam> ids, @RequestHeader("X-Token") String token) {
        return examService.delete(ids, token);
    }

    @PostMapping("/plan")
    public ExamPageResponse plan(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token) {
        return examService.plan(new Page<>(request.getPage(), request.getSize()), token, request.getKeyword());
    }
}
