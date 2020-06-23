package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.config.AlipayConfig;
import com.changgou.pay.config.RabbitMQConfig;
import com.changgou.pay.service.AlipayPayService;
import com.changgou.pay.service.WXPayService;
import com.changgou.util.ConvertUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/alipay")
@RestController
public class AlipayPayController {

    @Autowired
    private AlipayPayService alipayPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AlipayConfig alipayConfig;

    //下单
    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId") String orderId, @RequestParam("money") Integer money) {
        Map resultMap = alipayPayService.nativePay(orderId, money);
        return new Result(true, StatusCode.OK, "", resultMap);
    }

    @RequestMapping("/notify")
    public void notifyLogic(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付成功回调");

        String tradeStatus = request.getParameter("trade_status");
        System.out.println(tradeStatus);
        String orderId = request.getParameter("out_trade_no");

        //触发通知类型
//        通知类型	描述	默认开启
//        tradeStatus.TRADE_CLOSED	交易关闭	0
//        tradeStatus.TRADE_FINISHED	交易完结	0
//        tradeStatus.TRADE_SUCCESS	支付成功	1
//        tradeStatus.WAIT_BUYER_PAY	交易创建	0
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            //支付成功进行订单更新操作
            Map map = alipayPayService.queryOrder(orderId);
            // 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
            //TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
            // TRADE_SUCCESS（交易支付成功）、
            // TRADE_FINISHED（交易结束，不可退款）
            String trade_status = (String) map.get("trade_status");
            System.out.println("trade_status:" + trade_status);
            if ("TRADE_SUCCESS".equals(trade_status)) {
                Map message = new HashMap();
                message.put("orderId", request.getParameter("out_trade_no"));
                String transactionId = request.getParameter("trade_no");
                System.out.println("transactionId:" + transactionId);
                message.put("transactionId", transactionId);
                //发送消息完成订单状态变更
                rabbitTemplate.convertAndSend("", RabbitMQConfig.ORDER_PAY, JSON.toJSONString(message));
                //双向通信
                rabbitTemplate.convertAndSend("paynotify", "", request.getParameter("out_trade_no"));

            }
        }
    }


    //基于微信查询订单
    @GetMapping("/query/{orderId}")
    public Result queryOrder(@PathVariable("orderId") String orderId) {
        Map map = alipayPayService.queryOrder(orderId);
        return new Result(true, StatusCode.OK, "查询订单成功", map);
    }

    //基于微信关闭订单
    @PutMapping("/close/{orderId}")
    public Result closeOrder(@PathVariable("orderId") String orderId) {
        Map map = alipayPayService.closeOrder(orderId);
        return new Result(true, StatusCode.OK, "关闭订单成功", map);
    }
}
