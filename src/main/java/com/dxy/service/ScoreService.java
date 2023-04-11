package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Score;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.InsertResponse;

public interface ScoreService extends IService<Score> {
    InsertResponse insert(ScoreInsertRequest request);
}
