package com.ithl.eduorder.controller;


import com.ithl.commonutils.R;
import com.ithl.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
//@CrossOrigin
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    // 生产二维码
    @PostMapping("/createQRCode/{orderNo}")
    public R createQRCode(@PathVariable("orderNo") String orderNo) {
        Map map = payLogService.creteCode(orderNo);
        return R.ok().data(map);
    }

    // 查询订单支付的状态
    @GetMapping("/getPayStatus/{orderNo}")
    public R getPayStatus(@PathVariable("orderNo") String orderNo) {
        Map<String, String> map = payLogService.getPayStatusByOrderNo(orderNo);
        if (map == null) {
            return R.error().message("支付错误");
        }
        // 获取订单状态
        if (map.get("trade_state").equals("SUCCESS")) {
            // 添加状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中....");
    }
}

