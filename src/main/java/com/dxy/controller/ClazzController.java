package com.dxy.controller;

import com.dxy.pojo.Clazz;
import com.dxy.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/Clazz")
public class ClazzController {
    @Autowired
    private ClazzController clazzController;

    public R insert() {

        return null;
    }

}
