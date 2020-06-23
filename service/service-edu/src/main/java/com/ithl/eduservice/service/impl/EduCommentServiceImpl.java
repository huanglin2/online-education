package com.ithl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.commonutils.commentVo.EduCommentVo;
import com.ithl.eduservice.client.UcenterClient;
import com.ithl.eduservice.entity.EduComment;
import com.ithl.eduservice.entity.EduCourse;
import com.ithl.eduservice.mapper.EduCommentMapper;
import com.ithl.eduservice.service.EduCommentService;
import com.ithl.eduservice.service.EduCourseService;
import com.ithl.serviceutils.exchandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-26
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {
    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduCourseService courseService;

    @Override
    public void insertComment(String courseId, String memberId, String zhContext) {
        EduCommentVo userInfo = ucenterClient.getUserInfoComment(memberId);
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", courseId);
        EduCourse course = courseService.getOne(queryWrapper);
        String teacherId = course.getTeacherId();
        EduComment eduComment = new EduComment();
        eduComment.setAvatar(userInfo.getAvatar());
        eduComment.setContent(zhContext);
        eduComment.setCourseId(courseId);
        eduComment.setMemberId(memberId);
        eduComment.setNickname(userInfo.getNickname());
        eduComment.setTeacherId(teacherId);
        int insert = baseMapper.insert(eduComment);
        if (insert <= 0) {
            throw new MyException(1, "添加评论失败");
        }
    }
}
