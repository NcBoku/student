package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.NoticeMapper;
import com.dxy.pojo.Notice;
import com.dxy.response.NoticePageResponse;
import com.dxy.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public NoticePageResponse getPage(Page<Notice> page) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Notice::getTime);
        NoticePageResponse response = new NoticePageResponse();
        Page<Notice> noticePage = noticeMapper.selectPage(page, wrapper);
        response.setNotice(noticePage.getRecords());
        response.setTotalPage((int)noticePage.getPages());
        return response;
    }
}
