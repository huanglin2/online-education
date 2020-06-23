package com.ithl.eduservice.controller;


import com.ithl.commonutils.R;
import com.ithl.eduservice.entity.EduChapter;
import com.ithl.eduservice.entity.chapter.ChapterVo;
import com.ithl.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ithl.serviceutils.exchandler.MyException;

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
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    /**
     * 获取章节和小节
     *
     * @param courseId
     * @return
     */
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable("courseId") String courseId) {
        List<ChapterVo> chapterVoList = chapterService.getChapterVideo(courseId);
        return R.ok().data("allchapterVideo", chapterVoList);
    }

    /**
     * 添加章节
     */
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        boolean flag = chapterService.save(eduChapter);
        if (!flag) {
            throw new MyException(1, "添加章节失败");
        }
        String cid = eduChapter.getId();
        return R.ok().data("chapterId", cid);
    }

    /**
     * 根据章节id查询
     */
    @GetMapping("/getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable("chapterId") String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("eduChapter", eduChapter);
    }

    /**
     * 修改章节
     */
    @PostMapping("/updateChapterInfo")
    public R updateChapterInfo(@RequestBody EduChapter eduChapter) {
        boolean flag = chapterService.updateById(eduChapter);
        if (!flag) {
            throw new MyException(1, "修改失败");
        }
        return R.ok();
    }

    /**
     * 根据id进行删除
     * if有小节就不删除章节
     */
    @DeleteMapping("/deleteChapter/{chapterId}")
    public R deleteChapter(@PathVariable("chapterId") String chapterId) {
        Boolean flag = chapterService.deleteChapterIfNoVideo(chapterId);
        if (!flag) {
            return R.error().data("message", "请先删除章节下的小节");
        }
        return R.ok().data("message", "删除成功!");
    }
}
