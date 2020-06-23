package com.ithl.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithl.commonutils.R;
import com.ithl.eduservice.entity.EduCourse;
import com.ithl.eduservice.entity.vo.CourseQuery;
import com.ithl.eduservice.entity.vo.EduCourseVo;
import com.ithl.eduservice.entity.vo.PublicCourseInfo;
import com.ithl.eduservice.service.EduCourseService;
import com.ithl.serviceutils.exchandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //    查询所有课程列表
    @GetMapping("/getCourseList")
    public R getCourseList() {
        List<EduCourse> courseList = courseService.list(null);
        return R.ok().data("courseList", courseList);
    }

    // 查询课程带分页
    @GetMapping("/getCoursePage/{current}/{limit}")
    public R getCoursePage(@PathVariable("current") Long current, @PathVariable("limit") Long limit) {
        Page<EduCourse> coursePage = new Page<>(current, limit);
        courseService.page(coursePage, null);
        List<EduCourse> records = coursePage.getRecords();
        long total = coursePage.getTotal();
        return R.ok().data("rows", records).data("total", total);
    }

    // 查询课程带条件
    @PostMapping("/getCourseListPage/{current}/{limit}")
    public R getCourseListPage(@PathVariable("current") Long current,
                               @PathVariable("limit") Long limit,
                               @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> coursePage = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        // 多条件组合查询
        // 判断是否为空，不是就拼接
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        Integer lessonNum = courseQuery.getLessonNum();
        BigDecimal minPrice = courseQuery.getMinPrice();
        BigDecimal maxPrice = courseQuery.getMaxPrice();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(lessonNum)) {
            queryWrapper.eq("lesson_num", lessonNum);
        }
        if (!StringUtils.isEmpty(minPrice)) {
            queryWrapper.ge("price", minPrice);
        }
        if (!StringUtils.isEmpty(maxPrice)) {
            queryWrapper.le("price", maxPrice);
        }
        queryWrapper.orderByDesc("gmt_create");
        courseService.page(coursePage, queryWrapper);
        // 总记录数
        long total = coursePage.getTotal();
        // 数据list集合
        List<EduCourse> records = coursePage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    @DeleteMapping("/deleteCourseById/{courseId}")
    public R deleteCourseById(@PathVariable String courseId) {
        courseService.removeCourseById(courseId);
        return R.ok();
    }

    /*=========================以下为在添加课程中的操作==========================*/
    //    添加课程
    @PostMapping("/addCourseInfo")
    public R getCourseInfo(@RequestBody EduCourseVo eduCourseVo) {
        String courseId = courseService.saveInfo(eduCourseVo);
        return R.ok().data("courseId", courseId);
    }

    // 根据id查询课程
    @GetMapping("/getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable("id") String id) {
        EduCourseVo eduCourseVo = courseService.getCourseInfoById(id);
        return R.ok().data("eduCourseInfo", eduCourseVo);
    }

    // 修改课程
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody EduCourseVo eduCourseVo) {
        courseService.updateCourseInfo(eduCourseVo);
        return R.ok();
    }

    // 查询待发布的课程
    @GetMapping("/getPublishInfo/{courseId}")
    public R getPublishInfo(@PathVariable("courseId") String courseId) {
        PublicCourseInfo publicCourseInfo = courseService.getPublicCourseInfo(courseId);
        return R.ok().data("publicInfo", publicCourseInfo);
    }

    // 发布课程
    @PostMapping("publicCourse/{courseId}")
    public R publicCourse(@PathVariable String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        boolean flag = courseService.updateById(eduCourse);
        if (!flag) {
            throw new MyException(1, "发布失败");
        }
        return R.ok();
    }
}

