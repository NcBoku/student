package com.dxy.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamClazzroom {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer clazzroomId;

    private Integer examId;

    private Date start;

    private Date end;

    private Boolean isDeleted;
}
