package com.yiqzq.miaoshasystem.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yiqzq
 * @date 2020/6/19 17:47
 */
public interface FileService {
    String validateImage(MultipartFile file);
}
