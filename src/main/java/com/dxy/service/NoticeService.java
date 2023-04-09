package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Notice;

public interface NoticeService extends IService<Notice> {
    Page<Notice> getPage(Page<Notice> page);
}
