package com.ithl.oss.controller;

import com.ithl.commonutils.R;
import com.ithl.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hl
 */
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    // 上传头像的方法
    @PostMapping
    public R uploadFile(MultipartFile file) {
        // 获取文件
        String url = ossService.uploadAvatar(file);

        return R.ok().data("url", url);
    }
}
