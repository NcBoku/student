package com.dxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.Exam;
import com.dxy.response.ExamResponse;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
//    ExamResponse getStudentExams(Pagination pagination, @Param("gids") List<Integer> gids, @Param("cids")List<Integer> cids);
}
