package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        try {
            log.info("文件上传,{}", file);
            //获取上传文件名
            String originalFilename = file.getOriginalFilename();
            //获取拓展名
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            //拼接文件名
            String fileName = UUID.randomUUID().toString() + extension;
            log.info("新文件名为：{}", fileName);

            //保存上传文件到本地
            file.transferTo(new File("C:\\Users\\26706\\Desktop\\upload\\" + fileName));
            String path = "C:\\Users\\26706\\Desktop\\upload\\" + fileName;
            return Result.success(path);
        }catch (FileNotFoundException e) {
            log.info("文件上传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}