package com.ithl.eduorder.service;

import com.ithl.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String memberId);

}
