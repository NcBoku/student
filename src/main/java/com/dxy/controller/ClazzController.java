package com.dxy.controller;

import com.dxy.pojo.Clazz;
import com.dxy.pojo.R;
import com.dxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clazz")
public class ClazzController {
    @Autowired
    private ClazzService ClazzService;

    public R insert() {

        return null;
    }

}
