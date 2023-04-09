package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.pojo.Notice;
import com.dxy.request.NoticeGetRequest;
import com.dxy.response.NoticePageResponse;
import com.dxy.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/page")
    public NoticePageResponse getPage(@RequestBody NoticeGetRequest request){

        return noticeService.getPage(new Page(request.getPage(),request.getSize()));
    }
}
