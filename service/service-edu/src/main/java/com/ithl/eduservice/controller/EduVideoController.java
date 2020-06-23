package com.ithl.eduservice.controller;


import com.ithl.commonutils.R;
import com.ithl.eduservice.client.VodClient;
import com.ithl.eduservice.entity.EduVideo;
import com.ithl.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ithl.serviceutils.exchandler.MyException;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private VodClient vodClient;

    /**
     * 根据id查询小节
     */
    @GetMapping("/getVideoById/{videoId}")
    public R getVideoById(@PathVariable String videoId) {
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("videoInfo", video);
    }

    /**
     * 添加小节
     */
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        boolean flag = eduVideoService.save(eduVideo);
        if (!flag) {
            throw new MyException(1, "添加小节失败");
        }
        return R.ok();
    }

    /**
     * 通过id删除小节并删除视频
     */
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable("videoId") String videoId) {
        // 根据小节id查询视频id
        EduVideo eduVideo = eduVideoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            R r = vodClient.deleteAliyVideo(videoSourceId);
            if (r.getCode() == 1) {
                throw new MyException(1, "删除失败熔断器启动了");
            }
        }
        boolean flag = eduVideoService.removeById(videoId);
        if (!flag) {
            throw new MyException(1, "删除小节失败");
        }
        return R.ok();
    }

    // 根据courseId进行批删除
    @DeleteMapping("/deleteBatchVideo/{courseId}")
    public R deleteBatchVideo(@PathVariable String courseId) {
        eduVideoService.removeByCourseId(courseId);
        return R.ok();
    }

    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        boolean flag = eduVideoService.updateById(eduVideo);
        if (!flag) {
            throw new MyException(1, "修改小节成功");
        }
        return R.ok();
    }
}

