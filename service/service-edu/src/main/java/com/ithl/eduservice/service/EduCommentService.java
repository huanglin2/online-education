package com.ithl.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ithl.eduservice.entity.EduComment;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-26
 */
public interface EduCommentService extends IService<EduComment> {

    void insertComment(String courseId, String memberId, String zhContext);
}
