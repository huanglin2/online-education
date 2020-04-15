package com.ithl.eduservice.entity.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ithl.eduservice.entity.EduSubject;
import com.ithl.eduservice.service.EduSubjectService;
import com.ithl.sevicebase.exceptionhandler.MyException;
import org.springframework.util.StringUtils;

/**
 * @author hl
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    // SubjectExcelListener 不能交给spring 不能注入
    // 不能实现数据库操作

    public EduSubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // 一行一行读
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext context) {
        if (StringUtils.isEmpty(subjectData)) {
            throw new MyException(1, "文件数据为空");
        }
        EduSubject oneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (StringUtils.isEmpty(oneSubject)) {
            // 没有一级分类，进行添加
            oneSubject = new EduSubject();
            oneSubject.setParentId("0");
            oneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(oneSubject);
        }
        String pId = oneSubject.getId();
        // 有一级分类，添加二级分类
        EduSubject twoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pId);
        if (StringUtils.isEmpty(twoSubject)) {
            // 没有二级分类就添加
            twoSubject = new EduSubject();
            twoSubject.setTitle(subjectData.getTwoSubjectName());
            twoSubject.setParentId(pId);
            subjectService.save(twoSubject);
        }
    }

    // 一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", 0);
        EduSubject oneSubject = eduSubjectService.getOne(wrapper);
        return oneSubject;
    }

    // 二级分类可以重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String name, String pId) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pId);
        EduSubject twoSubject = eduSubjectService.getOne(wrapper);
        return twoSubject;
    }

    // 读完后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
