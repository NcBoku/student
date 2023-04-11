package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.NoticeMapper;
import com.dxy.pojo.Notice;
import com.dxy.pojo.User;
import com.dxy.response.InsertResponse;
import com.dxy.response.NoticePageResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.NoticeService;
import com.dxy.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public NoticePageResponse getPage(Page<Notice> page, String token) {
        User user = UserUtil.get(token);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();

        if (user != null && user.getType() != 0) {
            wrapper.orderByDesc(Notice::getTime)
                    .and(o -> o.eq(Notice::getType, user.getType()).or().eq(Notice::getType, 3));
        }

        NoticePageResponse response = new NoticePageResponse();
        Page<Notice> noticePage = noticeMapper.selectPage(page, wrapper);
        response.setNotice(noticePage.getRecords());
        response.setTotalPage((int) noticePage.getPages());
        response.setCode(20000);
        return response;
    }

    @Override
    public UpdateResponse update(Notice notice, String token) {
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);

        if (user != null && user.getType() != 0) {
            return response;
        }
        LambdaUpdateWrapper<Notice> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notice::getId, notice.getId());
        if (noticeMapper.update(notice, wrapper) == 1) {
            response.setCode(20000);
        }
        return response;
    }

    @Override
    public InsertResponse insert(Notice notice, String token) {
        User user = UserUtil.get(token);
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (user != null && user.getType() == 0 && noticeMapper.insert(notice) == 1) {
            response.setCode(20000);
        }
        return response;
    }
}
