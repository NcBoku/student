package com.dxy.util;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import com.dxy.pojo.Exam;
import com.dxy.response.ScoreExcel;
import com.dxy.response.StudentExcel;
import com.dxy.response.TeacherExcel;
import com.dxy.response.UpdateResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    public static void exportExcel(List<Map<String, Object>> map, HttpServletResponse response) {
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(map, ExcelType.XSSF);
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            fileName = URLEncoder.encode(fileName, "UTF8");
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportExamAndScoreExcel(HashMap<Exam, List<ScoreExcel>> exams, HttpServletResponse response) {
        System.out.println("util");
        List<Map<String, Object>> maps = new ArrayList<>();
        exams.forEach(
                (k, v) -> {
                    ExportParams exportParams = new ExportParams();
                    exportParams.setStyle(ExcelExportMyStylerImpl.class);
                    exportParams.setHeight((short) 8);
                    exportParams.setType(ExcelType.XSSF);
                    exportParams.setSheetName(k.getName());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("data", v);
                    map.put("title", exportParams);
                    map.put("entity", ScoreExcel.class);
                    maps.add(map);
                }
        );
        exportExcel(maps, response);
    }


    public static List<StudentExcel> importStudentExcel(MultipartFile file) {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        try{
            List<StudentExcel> scoreExcelList = ExcelImportUtil.importExcel(file.getInputStream(), StudentExcel.class, importParams);
            return scoreExcelList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<TeacherExcel> importTeacherExcel(MultipartFile file) {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        try{
            List<TeacherExcel> scoreExcelList = ExcelImportUtil.importExcel(file.getInputStream(), TeacherExcel.class, importParams);
            return scoreExcelList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
