package com.ithl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.eduservice.entity.EduChapter;
import com.ithl.eduservice.entity.EduVideo;
import com.ithl.eduservice.entity.chapter.ChapterVo;
import com.ithl.eduservice.entity.chapter.VideoVo;
import com.ithl.eduservice.mapper.EduChapterMapper;
import com.ithl.eduservice.service.EduChapterService;
import com.ithl.eduservice.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ithl.serviceutils.exchandler.MyException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    /**
     * 根据课程id查询
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterVideo(String courseId) {
        // 章节
        QueryWrapper<EduChapter> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        List<EduChapter> chapterList = baseMapper.selectList(queryWrapper1);
        // 小节
        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        List<EduVideo> videoList = eduVideoService.list(queryWrapper2);
        List<ChapterVo> finalChapterVoList = new ArrayList<>();
        for (int i = 0; i < chapterList.size(); i++) {
            EduChapter eduChapter = chapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int m = 0; m < videoList.size(); m++) {
                EduVideo eduVideo = videoList.get(m);
                if (eduVideo.getChapterId().equals(chapterVo.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
            finalChapterVoList.add(chapterVo);
        }
        return finalChapterVoList;
    }

    @Override
    public Boolean deleteChapterIfNoVideo(String chapterId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(queryWrapper);
        if (count > 0) {
            // 有小节不删除
            throw new MyException(1, "请先删除章节下的小节");
        } else {
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }
    }

    // 根据课程id 删除章节
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        int result = baseMapper.delete(wrapper);
        System.out.println(result);
    }
}
