package com.ithl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.eduservice.entity.EduCourse;
import com.ithl.eduservice.entity.EduCourseDescription;
import com.ithl.eduservice.entity.EduTeacher;
import com.ithl.eduservice.entity.frontvo.CourseDetailInfoVo;
import com.ithl.eduservice.entity.frontvo.CourseFrontVo;
import com.ithl.eduservice.entity.vo.EduCourseVo;
import com.ithl.eduservice.entity.vo.PublicCourseInfo;
import com.ithl.eduservice.mapper.EduCourseMapper;
import com.ithl.eduservice.service.*;
import com.ithl.serviceutils.exchandler.MyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduTeacherService teacherService;

    @Override
    public String saveInfo(EduCourseVo eduCourseVo) {
        // 向eduCourse添加数据
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(eduCourseVo, eduCourse);

        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0) {
            throw new MyException(1, "添加课程失败");
        }

        String cid = eduCourse.getId();
        // 向eduCourseDescription 添加数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(eduCourseVo.getDescription());
        eduCourseDescription.setId(cid);
        boolean isSuccess = eduCourseDescriptionService.save(eduCourseDescription);
        if (!isSuccess) {
            throw new MyException(1, "课程描述添加失败");
        }
        return cid;
    }

    @Override
    public EduCourseVo getCourseInfoById(String id) {
        EduCourseVo eduCourseVo = new EduCourseVo();
        EduCourse eduCourse = baseMapper.selectById(id);
        BeanUtils.copyProperties(eduCourse, eduCourseVo);
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(id);
        eduCourseVo.setDescription(courseDescription.getDescription());
        return eduCourseVo;
    }

    @Override
    public void updateCourseInfo(EduCourseVo eduCourseVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(eduCourseVo, eduCourse);
        int result = baseMapper.updateById(eduCourse);
        if (result == 0) {
            throw new MyException(1, "修改失败1");
        }
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourseVo.getId());
        eduCourseDescription.setDescription(eduCourseVo.getDescription());
        boolean flag = eduCourseDescriptionService.updateById(eduCourseDescription);
        if (!flag) {
            throw new MyException(1, "修改失败2");
        }
    }

    @Override
    public PublicCourseInfo getPublicCourseInfo(String courseId) {
        PublicCourseInfo publicCourseInfo = baseMapper.getPublicCourseInfo(courseId);
        return publicCourseInfo;
    }

    // 根据课程id删除课程 先删小节，然后章节，然后描述，最后再删课程
    @Override
    public void removeCourseById(String courseId) {
        // 1小节
        eduVideoService.removeByCourseId(courseId);
        // 2章节
        chapterService.removeByCourseId(courseId);
        // 3描述
        eduCourseDescriptionService.removeById(courseId);
        // 4删除课程
        int result = baseMapper.deleteById(courseId);
        if (result == 0) {
            throw new MyException(1, "删除失败");
        }
    }

    @Override
    @Cacheable(key = "'selectTeacherList'", value = "teacherList")
    public List<EduTeacher> cacheTeacherList() {
        // 查询前四名师
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(wrapper);
        return teacherList;
    }

    @Override
    @Cacheable(key = "'selectCourseList'", value = "courseList")
    public List<EduCourse> cacheCourseList() {
        // 查询前八条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduList = baseMapper.selectList(wrapper);
        return eduList;
    }

    @Override
    public Map<String, Object> getCourseFront(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String title = courseFrontVo.getTitle();
        String priceSort1 = courseFrontVo.getPriceSort1();
        String priceSort2 = courseFrontVo.getPriceSort2();
        String gmtCreateSort = courseFrontVo.getGmtCreateSort();
        String buyCountSort = courseFrontVo.getBuyCountSort();
        String teacherId = courseFrontVo.getTeacherId();
        String subjectId = courseFrontVo.getSubjectId();
        String subjectParentId = courseFrontVo.getSubjectParentId();
        // 判断一级分类
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }
        // 判断二级分类
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }
        // 判断课程名称
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        // 查询指定老师
        if (!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id", teacherId);
        }
        // 关注度排序
        if (!StringUtils.isEmpty(buyCountSort)) {
            wrapper.orderByDesc("buy_count");
        }
        // 价格升序排序
        if (!StringUtils.isEmpty(priceSort1)) {
            wrapper.orderByAsc("price");
        }
        // 价格降序排序
        if (!StringUtils.isEmpty(priceSort2)) {
            wrapper.orderByDesc("price");
        }
        // 创建时间排序
        if (!StringUtils.isEmpty(gmtCreateSort)) {
            wrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(coursePage, wrapper);
        List<EduCourse> records = coursePage.getRecords();
        long total = coursePage.getTotal();
        long size = coursePage.getSize();
        long pages = coursePage.getPages();
        long current = coursePage.getCurrent();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("total", total);
        map.put("size", size);
        map.put("page", pages);
        map.put("current", current);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public CourseDetailInfoVo getBaseCourseInfo(String courseId) {
        return baseMapper.getCourseInfoByCourseId(courseId);
    }
}
