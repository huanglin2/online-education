package com.ithl.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ithl.eduservice.entity.EduSubject;
import com.ithl.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-14
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) throws IOException;

    List<OneSubject> getAllOneTwoSubject();
}
