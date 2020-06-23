package com.ithl.staservice.controller;


import com.ithl.commonutils.R;
import com.ithl.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-29
 */
//@CrossOrigin
@RestController
@RequestMapping("/staservice/stadaily")
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService dailyService;

    // 查询当天注册人数生产数据加入表中
    @PostMapping("/registerCount/{day}")
    public R registerCount(@PathVariable("day") String day) {
        // 获取人数并存入
        int result = dailyService.registerCount(day);
        return R.ok().data("count", result);
    }

    // 图标显示
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable("type") String type, @PathVariable("begin") String begin, @PathVariable("end") String end) {
        Map<String, Object> map = dailyService.getShowDate(type, begin, end);
        return R.ok().data(map);
    }
}

