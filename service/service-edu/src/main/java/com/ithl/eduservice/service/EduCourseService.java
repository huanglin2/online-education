package com.ithl.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ithl.eduservice.entity.EduCourse;
import com.ithl.eduservice.entity.EduTeacher;
import com.ithl.eduservice.entity.frontvo.CourseDetailInfoVo;
import com.ithl.eduservice.entity.frontvo.CourseFrontVo;
import com.ithl.eduservice.entity.vo.EduCourseVo;
import com.ithl.eduservice.entity.vo.PublicCourseInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveInfo(EduCourseVo eduCourseVo);

    EduCourseVo getCourseInfoById(String id);

    void updateCourseInfo(EduCourseVo eduCourseVo);

    PublicCourseInfo getPublicCourseInfo(String courseId);

    void removeCourseById(String courseId);

    List<EduTeacher> cacheTeacherList();

    List<EduCourse> cacheCourseList();

    Map<String, Object> getCourseFront(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    CourseDetailInfoVo getBaseCourseInfo(String courseId);

}
