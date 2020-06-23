package com.ithl.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.staservice.client.UCenterClient;
import com.ithl.staservice.entity.StatisticsDaily;
import com.ithl.staservice.mapper.StatisticsDailyMapper;
import com.ithl.staservice.service.StatisticsDailyService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-29
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UCenterClient centerClient;

    //     1. 查询当天注册人数 courseNum课程购买数量 video_view_count课程浏览数量2. 放入表中
    @Override
    public int registerCount(String day) {
        // 先删除表中日期相同的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);
        // 获取数据存入表中
        int registerCount = centerClient.getRegisterCount(day);
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(registerCount);
        statisticsDaily.setDateCalculated(day);
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100, 300));
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(200, 400));
        statisticsDaily.setCourseNum(RandomUtils.nextInt(10, 20));
        int insert = baseMapper.insert(statisticsDaily);
        return insert;
    }

    // 查询显示 用list返回 前端就是数组形式
    @Override
    public Map<String, Object> getShowDate(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(begin)) {
            wrapper.ge("date_calculated", begin);
        }
        if (StringUtils.isEmpty(end)) {
            wrapper.le("date_calculated", end);
        }
        wrapper.select("date_calculated", type);
        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);
        List<String> dateList = new ArrayList<>();
        List<Integer> numDateList = new ArrayList<>();
        for (int i = 0; i < dailyList.size(); i++) {
            StatisticsDaily daily = dailyList.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "login_num":
                    numDateList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDateList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDateList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDateList.add(daily.getCourseNum());
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculateList", dateList);
        map.put("numDataList", numDateList);
        return map;
    }

}
