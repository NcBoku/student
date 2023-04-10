package com.dxy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
public class UploadController {
    //获取yaml中配置的上传路径属性
    @Value(("${web.upload-path}"))
    private String uploadPath;

    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();  //获取文件原名
        String visibleUri="/"+fileName;                //拼接访问图片的地址
        String saveUri=uploadPath+"/"+fileName;        //拼接保存图片的真实地址

        System.out.println("图片原文件名="+fileName+"图片访问地址="+visibleUri+" 图片保存真实地址={}"+saveUri);

        File saveFile = new File(saveUri);
        //判断是否存在文件夹，不存在就创建
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            file.transferTo(saveFile);  //保存文件到真实存储路径下
        } catch (IOException e) {
            e.printStackTrace();
        }

        return visibleUri;
    }

}