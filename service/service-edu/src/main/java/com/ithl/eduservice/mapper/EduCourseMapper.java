package com.ithl.eduservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ithl.eduservice.entity.EduCourse;
import com.ithl.eduservice.entity.frontvo.CourseDetailInfoVo;
import com.ithl.eduservice.entity.vo.PublicCourseInfo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    PublicCourseInfo getPublicCourseInfo(String CourseId);

    CourseDetailInfoVo getCourseInfoByCourseId(String courseId);

}
