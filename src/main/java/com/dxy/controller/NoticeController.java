package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.dxy.request.NoticeGetRequest;
import com.dxy.response.NoticePageResponse;
import com.dxy.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/page")
    public NoticePageResponse getPage(@RequestBody NoticeGetRequest request){

        return noticeService.getPage(new Page(request.getPage(),request.getSize()));
    }
}
