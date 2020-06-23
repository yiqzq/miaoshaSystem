package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author yiqzq
 * @date 2020/6/19 17:02
 */
@Slf4j
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping(value = "/test/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        String s = fileService.validateImage(file);
        if (s == null) throw new MyException(CodeMsg.FILE_UPLOAD_ERROR);
        return s;
    }
}