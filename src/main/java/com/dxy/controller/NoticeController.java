package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.dxy.request.NoticeGetRequest;
import com.dxy.response.NoticePageResponse;
import com.dxy.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/list")
    public NoticePageResponse list(@RequestBody NoticeGetRequest request, HttpServletRequest r) {

        return noticeService.getPage(new Page(request.getPage(), request.getSize()), r.getHeader("X-Token"));
    }
}
