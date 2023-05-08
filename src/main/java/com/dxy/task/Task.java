package com.dxy.task;

import com.dxy.service.ClazzroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Task {

    @Autowired
    private ClazzroomService clazzroomService;

    // 每隔1个小时,检查是否有考试开始,即start< now < end
    // 如果考试开始,设置对应的考试和教室为占用状态
    // 如果有考试结束,则设置exam_classroom为空闲状态,考试则设置为结束状态
    @Scheduled(cron = "0 */1 * * * ?")
    public void execute(){
        clazzroomService.updateInfo();
    }
}
