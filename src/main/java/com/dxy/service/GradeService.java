package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Grade;
import com.dxy.request.GradeUpdateRequest;
import com.dxy.request.PageGetRequest;
import com.dxy.response.GradePageResponse;
import com.dxy.response.GradesResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface GradeService extends IService<Grade> {
    InsertResponse insert(GradeUpdateRequest grade, String token);
    UpdateResponse update(GradeUpdateRequest grade,String token);
    UpdateResponse delete(List<Grade> grade, String token);
    GradesResponse getGradesByExamId(@PathVariable("examId")String id, @RequestHeader("X-Token") String token);
    GradePageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token);
}
