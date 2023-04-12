package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.dxy.pojo.Notice;
import com.dxy.request.NoticeGetRequest;
import com.dxy.request.NoticeInsertRequest;
import com.dxy.request.NoticeUpdateRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.NoticePageResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/list")
    public NoticePageResponse list(@RequestBody NoticeGetRequest request, HttpServletRequest r) {

        return noticeService.getPage(request, r.getHeader("X-Token"));
    }

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody NoticeInsertRequest notice, HttpServletRequest r) {
        Notice n = new Notice();
        BeanUtils.copyProperties(notice, n);
        return noticeService.insert(n, r.getHeader("X-Token"));
    }

    @PostMapping("/update")
    public UpdateResponse update(@RequestBody NoticeUpdateRequest notice, HttpServletRequest r) {
        Notice n = new Notice();
        BeanUtils.copyProperties(notice, n);
        return noticeService.update(n, r.getHeader("X-Token"));
    }

    @PostMapping("/delete")
    public UpdateResponse delete(@RequestBody List<Notice> notices, @RequestHeader("X-Token") String token) {
        return noticeService.delete(notices, token);
    }
}
