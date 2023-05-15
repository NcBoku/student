package com.dxy.response;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TeacherExcel {
    @Excel(name="学号")
    private String number;
    @Excel(name="姓名")
    private String name;
    @Excel(name="性别")
    private String sex;
    @Excel(name="手机号")
    private String phone;
    @Excel(name="qq")
    private String qq;
    @Excel(name="课程")
    private String courses;
}
