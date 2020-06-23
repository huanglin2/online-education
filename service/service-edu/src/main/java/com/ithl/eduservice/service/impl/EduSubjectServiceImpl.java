package com.ithl.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.eduservice.entity.EduSubject;
import com.ithl.eduservice.entity.excel.SubjectData;
import com.ithl.eduservice.entity.excel.SubjectExcelListener;
import com.ithl.eduservice.entity.subject.OneSubject;
import com.ithl.eduservice.entity.subject.TwoSubject;
import com.ithl.eduservice.mapper.EduSubjectMapper;
import com.ithl.eduservice.service.EduSubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) throws IOException {

        // 文件输入流
        InputStream in = file.getInputStream();
        EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 一级 pid =0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", 0);
        List<EduSubject> oneSubjects = baseMapper.selectList(wrapperOne);
        // 二级 pid != 0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", 0);
        List<EduSubject> twoSubjects = baseMapper.selectList(wrapperTwo);

        List<OneSubject> finalSubjectList = new ArrayList<>();
     /*
        一个一级分类包含多个二级分类
            id :1
            label : hh
            children: [{
                id: 1-1
                label: xx
            },{
                id: 1-2
                label: xx
            }
            ]
         */
        for (int i = 0; i < oneSubjects.size(); i++) {
            EduSubject eduSubject = oneSubjects.get(i);
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduSubject, oneSubject);
            List<TwoSubject> twoSubjectList = new ArrayList<>();
            for (int m = 0; m < twoSubjects.size(); m++) {
                EduSubject eduSubject1 = twoSubjects.get(m);
                // 判断是否匹配
                if (eduSubject1.getParentId().equals(oneSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject1, twoSubject);
                    twoSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjectList);
            finalSubjectList.add(oneSubject);
        }
        return finalSubjectList;
    }
}
