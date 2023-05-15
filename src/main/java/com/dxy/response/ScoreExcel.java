package com.dxy.response;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ScoreExcel implements Serializable {
    @Excel(name="考场", width = 20, orderNum = "1")
    private String name;
    @Excel(name = "开始时间", width = 20, orderNum = "2")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date start;
    @Excel(name = "结束时间", width = 20, orderNum = "3")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date end;
    @Excel(name="监考老师", width = 20, orderNum = "4")
    private String teachers;
    @Excel(name="学号", width = 20, orderNum = "4")
    private String number;
    @Excel(name="姓名", width = 20, orderNum = "5")
    private String student;
    @Excel(name="课程", width = 20, orderNum = "6")
    private String course;
    @Excel(name="班级", width = 20, orderNum = "7")
    private String clazz;
    @Excel(name="平时分", width = 20, orderNum = "8")
    private Integer pscore;
    @Excel(name="考试分", width = 20, orderNum = "9")
    private Integer score;
    @Excel(name="评价", width = 20, orderNum = "10")
    private String level;
}
