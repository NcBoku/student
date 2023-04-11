package com.dxy.response;

import com.dxy.pojo.Exam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ExamPageResponse {
    private Integer code;

    private List<ExamResponse> exams;

    private Integer totalPage;
}

