package com.ithl.eduservice.controller;


import com.ithl.commonutils.R;
import com.ithl.eduservice.entity.subject.OneSubject;
import com.ithl.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-14
 */
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    // 添加课程分类  获取上传过来的文件，把文件内容读取出来
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file) {
        // 上传过来的excel文件
        try {
            subjectService.saveSubject(file, subjectService);
            return R.ok();
        } catch (Exception e) {
            return R.error();
        }
    }

    @GetMapping("/getSubject")
    public R getSubject() {
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list", list);
    }
}

