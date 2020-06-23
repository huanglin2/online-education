package com.ithl.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ithl.commonutils.JwtUtils;
import com.ithl.commonutils.R;
import com.ithl.eduorder.entity.Order;
import com.ithl.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
//@CrossOrigin
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*生成订单*/
    @PostMapping("/generate/{courseId}")
    public R generateOrder(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo", orderNo);
    }

    /*根据订单id 查询订单*/
    @GetMapping("/getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable("orderNo") String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(queryWrapper);
        return R.ok().data("order", order);
    }

    // 根据课程id和用户id查询订单状态
    @GetMapping("/isBuyCourse")
    public boolean isBuyCourse(@RequestParam("courseId") String courseId, @RequestParam("memberId") String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        int count = orderService.count(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }
}