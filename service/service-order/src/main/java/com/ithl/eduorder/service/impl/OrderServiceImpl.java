package com.ithl.eduorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ithl.commonutils.orderVo.CourseDetailInfoOrder;
import com.ithl.commonutils.orderVo.UcenterMemberOrder;
import com.ithl.eduorder.client.EduCourseClient;
import com.ithl.eduorder.client.UCenterClient;
import com.ithl.eduorder.entity.Order;
import com.ithl.eduorder.mapper.OrderMapper;
import com.ithl.eduorder.service.OrderService;
import com.ithl.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduCourseClient courseClient;
    @Autowired
    private UCenterClient centerClient;

    // 生成订单
    @Override
    public String createOrders(String courseId, String memberId) {
        // 远程根据courseId获取课程
        CourseDetailInfoOrder courseInfoOrder = courseClient.getCourseInfoOrder(courseId);
        // 远程根据memberId获取用户
        UcenterMemberOrder userInfoOrder = centerClient.getUserInfoOrder(memberId);
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); // 订单号
        order.setCourseId(courseId);  // 课程id
        order.setCourseTitle(courseInfoOrder.getTitle()); // 课程标题
        order.setCourseCover(courseInfoOrder.getCover()); // 课程封面
        order.setTeacherName(courseInfoOrder.getTeacherName()); // 讲师名称
        order.setTotalFee(courseInfoOrder.getPrice());  // 价格
        order.setMemberId(memberId); // 用户Id
        order.setMobile(userInfoOrder.getMobile()); //用户手机
        order.setNickname(userInfoOrder.getNickname()); // 用户昵称
        order.setStatus(0); // 订单状态 0 未 1已
        order.setPayType(1); // 支付类型
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
