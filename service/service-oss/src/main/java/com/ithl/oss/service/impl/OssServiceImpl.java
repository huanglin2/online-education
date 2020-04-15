package com.ithl.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ithl.oss.service.OssService;
import com.ithl.oss.utils.ConstandPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author hl
 */
@Service
public class OssServiceImpl implements OssService {
    /**
     * 上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadAvatar(MultipartFile file) {

        String endpoint = ConstandPropertiesUtils.END_POINT;
        String accessKeyId = ConstandPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstandPropertiesUtils.ACCESS_KEY_SECRET;
        String buketName = ConstandPropertiesUtils.BUCKET_NAME;

        // 创建oss实列
        OSS ossClient = null;
        InputStream inputStream = null;
        String fileName;

        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            inputStream = file.getInputStream();
            fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;

            // 按文件时间进行分类存储
            String filePath = new DateTime().toString("yyyy/MM/dd");
            fileName = filePath + fileName;
            // 调用oss实现上传  buket名称    上传到oos文件路径和名称  上传文件输入流
            ossClient.putObject(buketName, fileName, inputStream);
            //https://online-education1.oss-cn-beijing.aliyuncs.com/%E7%92%80%E7%92%A8%E6%98%9F%E7%A9%BA4k8k%E8%B6%85%E9%AB%98%E6%B8%85%E5%A3%81%E7%BA%B811-1024x576.jpg

            String url = "https://" + buketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
    }
}
