package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Exam;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.response.ExamPageResponse;
import com.dxy.response.ExamScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ExamService extends IService<Exam> {
    ExamPageResponse list(Page<Exam> page,String token);
    InsertResponse insert(ExamInsertRequest request, String token);
    ExamScoreResponse score(ExamScoreRequest request,String token);
    UpdateResponse update(@RequestBody ExamInsertRequest request, @RequestHeader("X-Token") String token);
}
