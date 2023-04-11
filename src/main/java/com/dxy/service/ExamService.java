package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Exam;
import com.dxy.response.ExamPageResponse;

public interface ExamService extends IService<Exam> {
    ExamPageResponse list(Page<Exam> page,String token);
}
