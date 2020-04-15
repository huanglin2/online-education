package com.ithl.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author hl
 */
public interface OssService {
    String uploadAvatar(MultipartFile file);
}
