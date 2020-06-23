package com.ithl.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import com.ithl.eduorder.entity.Order;
import com.ithl.eduorder.entity.PayLog;
import com.ithl.eduorder.mapper.PayLogMapper;
import com.ithl.eduorder.service.OrderService;
import com.ithl.eduorder.service.PayLogService;
import com.ithl.eduorder.utils.HttpClient;
import com.ithl.serviceutils.exchandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author hl
 * @since 2020-04-27
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Override
    public Map creteCode(String orderNo) {
        try {
            // 1. 查询出订单
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(queryWrapper);
            // 2.设置支付参数
            Map map = new HashMap<>();
            map.put("appid", "wx74862e0dfcf69954");
            map.put("mch_id", "1558950191");
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", order.getCourseTitle());
            map.put("out_trade_no", orderNo);
            map.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");
            // 3、发送请求 地址固定的
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(map, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            // 4、得到发送请求的结果
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、封装返回结果集
            Map map1 = new HashMap<>();
            map1.put("out_trade_no", orderNo);
            map1.put("course_id", order.getCourseId());
            map1.put("total_fee", order.getTotalFee());
            map1.put("result_code", resultMap.get("result_code"));
            map1.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(1, "生成失败");
        }
    }

    @Override
    public Map<String, String> getPayStatusByOrderNo(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        String orderNo = map.get("out_trade_no");
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);
        if (order.getStatus().intValue() == 1) {
            return;
        } else {
            // 1 已支付 0. 未支付
            order.setStatus(1);
            orderService.updateById(order);
            PayLog payLog = new PayLog();
            payLog.setOrderNo(order.getOrderNo());//支付订单号
            payLog.setPayTime(new Date());
            payLog.setPayType(1);//支付类型
            payLog.setTotalFee(order.getTotalFee());//总金额(分)
            payLog.setTradeState(map.get("trade_state"));//支付状态
            payLog.setTransactionId(map.get("transaction_id"));
            payLog.setAttr(JSONObject.toJSONString(map));
            baseMapper.insert(payLog);
        }
    }
}
