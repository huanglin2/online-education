package com.ithl.eduorder.service;

import com.ithl.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
public interface PayLogService extends IService<PayLog> {

    Map creteCode(String orderNo);

    // 湖片区订单状态
    Map<String, String> getPayStatusByOrderNo(String orderNo);

    // 添加支付记录并更新支付状态
    void updateOrderStatus(Map<String, String> map);
}
