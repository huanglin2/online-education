package com.ithl.eduservice.service;

import com.ithl.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);

}
