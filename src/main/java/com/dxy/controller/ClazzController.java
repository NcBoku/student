package com.dxy.controller;

import com.dxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clazz")
@CrossOrigin
public class ClazzController {
    @Autowired
    private ClazzService ClazzService;

    public void insert() {

    }

}
