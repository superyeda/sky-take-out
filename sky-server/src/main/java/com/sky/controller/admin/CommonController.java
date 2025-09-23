package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("上传图片：{}" ,file);
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        String fileUrl = null;
        try {
            fileUrl = aliOssUtil.upload(file.getBytes(), filename);
            return Result.success(fileUrl);
        } catch (IOException e) {
            log.error("文件上传失败：{}" , e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);




    }
}
