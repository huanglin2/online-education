package com.ithl.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ithl.commonutils.JwtUtils;
import com.ithl.commonutils.R;
import com.ithl.eduservice.entity.EduComment;
import com.ithl.eduservice.service.EduCommentService;
import com.ithl.serviceutils.exchandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-26
 */
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    // 添加评论
    @PostMapping("/addComment/{courseId}")
    public R addComment(@RequestBody String context,
                        @PathVariable("courseId") String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        commentService.insertComment(courseId, memberId, context);
        return R.ok();
    }

    // 获取评论
    @GetMapping("/getComment/{current}/{limit}/{courseId}")
    public R getComment(
            @PathVariable("current") long current,
            @PathVariable("limit") long limit, @PathVariable("courseId") String courseId) {
        Page<EduComment> page = new Page<>(current, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        commentService.page(page, wrapper);
        List<EduComment> records = page.getRecords();
        long total = page.getTotal();
        long size = page.getSize();
        long pageCurrent = page.getCurrent();
        long pages = page.getPages();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("total", total);
        map.put("size", size);
        map.put("pageCurrent", pageCurrent);
        map.put("pages", pages);
        return R.ok().data(map);
    }

    // 删除评论
    @DeleteMapping("/deleteComment/{cid}")
    public R deleteComment(@PathVariable("cid") String cid) {
        boolean flag = commentService.removeById(cid);
        if (!flag) {
            throw new MyException(1, "删除失败");
        } else {
            return R.ok().message("删除成功");
        }
    }
}

