package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Notice;
import com.dxy.response.NoticePageResponse;

public interface NoticeService extends IService<Notice> {
    NoticePageResponse getPage(Page<Notice> page,String token);
}
