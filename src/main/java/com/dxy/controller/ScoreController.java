package com.dxy.controller;

import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.InsertResponse;
import com.dxy.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/score")
@CrossOrigin
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping("/insert")
    public InsertResponse insert(@RequestBody ScoreInsertRequest request){
        return scoreService.insert(request);
    }
}
