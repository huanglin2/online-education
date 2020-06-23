package com.ithl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.eduservice.client.VodClient;
import com.ithl.eduservice.entity.EduVideo;
import com.ithl.eduservice.mapper.EduVideoMapper;
import com.ithl.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    // 根据课程id删除小节
    @Override
    public void removeByCourseId(String courseId) {
        // 根据courseId查询视频id
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.select("video_source_id");
        List<EduVideo> eduVideoListIds = baseMapper.selectList(queryWrapper);
        // 把List<EduVideo>转成List<String>
        List<String> videoIds = new ArrayList<>();

        for (int i = 0; i < eduVideoListIds.size(); i++) {
            EduVideo eduVideo = eduVideoListIds.get(i);
            if (!StringUtils.isEmpty(eduVideo)) {
                String videoSourceId = eduVideo.getVideoSourceId();
                videoIds.add(videoSourceId);
            }
        }

        if (videoIds.size() > 0) {
            // 根据多个视频id删除视频
            vodClient.deleteBatch(videoIds);
        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
