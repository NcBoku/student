package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Notice;
import com.dxy.response.InsertResponse;
import com.dxy.response.NoticePageResponse;
import com.dxy.response.UpdateResponse;
import org.apache.ibatis.annotations.Update;

public interface NoticeService extends IService<Notice> {
    NoticePageResponse getPage(Page<Notice> page,String token);
    UpdateResponse update(Notice notice,String token);
    InsertResponse insert(Notice notice,String token);
}
