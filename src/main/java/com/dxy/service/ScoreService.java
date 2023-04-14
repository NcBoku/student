package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Score;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.CourseGradeClazzScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface ScoreService extends IService<Score> {
    InsertResponse insert(ScoreInsertRequest request);
    CourseGradeClazzScoreResponse courseClazzScore(String examId,String token);
    UpdateResponse update(@RequestBody List<Score> scoreList, @RequestHeader("X-Token") String token);
}
