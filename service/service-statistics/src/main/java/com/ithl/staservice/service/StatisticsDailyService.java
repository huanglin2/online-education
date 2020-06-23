package com.ithl.staservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ithl.staservice.entity.StatisticsDaily;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-29
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    int registerCount(String day);


    Map<String, Object> getShowDate(String type, String begin, String end);
}
