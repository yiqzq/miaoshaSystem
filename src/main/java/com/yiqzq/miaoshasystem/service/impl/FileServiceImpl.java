package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author yiqzq
 * @date 2020/6/19 17:47
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final long IMAGE_SIZE = 2000000;

    @Override
    public String validateImage(MultipartFile file) {
        if (file.isEmpty()) throw new MyException(CodeMsg.FILE_IS_EMPTY);
        if (file.getSize() > IMAGE_SIZE) {
            throw new MyException(CodeMsg.FILE_IS_TOO_BIG);
        }
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file.getInputStream());
            if (bi == null) {
                throw new MyException(CodeMsg.FILE_FORMAT_IS_ERROR);
            }

            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("上传的文件名为：" + fileName + " 后缀名为" + suffixName);
            // 设置文件存储路径(F盘),你可以存放在你想要指定的路径里面。
//            String filePath = "F:\\image\\";
            String filePath = "/usr/local/img/";
            String path = filePath + fileName;
            log.info("path:" + path);
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return "/userimg/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
