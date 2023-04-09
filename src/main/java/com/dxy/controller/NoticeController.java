package com.dxy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxy.pojo.Notice;
import com.dxy.request.NoticeGetRequest;
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
    public List<Notice> getPage(@RequestBody NoticeGetRequest request){
        Page<Notice> noticePage = new Page<Notice>();
        noticePage.setSize(request.getSize());
        noticePage.setPages(request.getPage());
        return noticeService.getPage(noticePage).getRecords();
    }
}
