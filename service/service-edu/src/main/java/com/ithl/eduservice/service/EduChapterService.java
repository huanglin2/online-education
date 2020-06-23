package com.ithl.eduservice.service;

import com.ithl.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ithl.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideo(String courseId);


    Boolean deleteChapterIfNoVideo(String chapterId);

    void removeByCourseId(String courseId);
}
