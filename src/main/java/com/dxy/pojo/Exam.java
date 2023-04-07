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
public class Exam {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Date time;

    private String remark;

    private Integer type;

    private Integer gradeId;

    private Integer clazzId;

    private Integer courseId;
}
