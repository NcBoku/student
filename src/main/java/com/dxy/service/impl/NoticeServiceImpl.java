package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.NoticeMapper;
import com.dxy.pojo.Notice;
import com.dxy.pojo.User;
import com.dxy.request.NoticeGetRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.NoticePageResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.NoticeService;
import com.dxy.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public NoticePageResponse getPage(NoticeGetRequest request, String token) {
        Page page = new Page(request.getPage(), request.getSize());
        User user = UserUtil.get(token);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();

        if (user != null && user.getType() != 0) {
            wrapper.orderByDesc(Notice::getTime)
                    .and(o -> o.eq(Notice::getType, user.getType()).or().eq(Notice::getType, 3));
        } else {
            wrapper.orderByDesc(Notice::getTime);
        }

        if (request.getKeyword() != null && !request.getKeyword().equals("")) {
            wrapper.and(
                    o -> o.like(Notice::getTitle, request.getKeyword())
                            .or()
                            .like(Notice::getContent, request.getKeyword())
                            .or()
                            .like(Notice::getSender, request.getKeyword())
                            .or()
                            .like(Notice::getId,request.getKeyword())
            );
        }

        NoticePageResponse response = new NoticePageResponse();
        Page<Notice> noticePage = noticeMapper.selectPage(page, wrapper);
        response.setNotice(noticePage.getRecords());
        response.setTotalPage((int) noticePage.getTotal());
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
        Notice old = noticeMapper.selectOne(new LambdaQueryWrapper<Notice>().eq(Notice::getId, notice.getId()));
        notice.setTime(old.getTime());
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
        notice.setTime(new Date());
        if (user != null && user.getType() == 0 && noticeMapper.insert(notice) == 1) {
            response.setCode(20000);
        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Notice> notices, String token) {
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        ArrayList<Integer> list = new ArrayList<>();
        notices.forEach(e -> {
            list.add(e.getId());
        });
        response.setCode(20001);
        if (user.getType() == 0) {
            int i = noticeMapper.delete(new LambdaQueryWrapper<Notice>().in(Notice::getId, list));
            if (i == notices.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }


}
