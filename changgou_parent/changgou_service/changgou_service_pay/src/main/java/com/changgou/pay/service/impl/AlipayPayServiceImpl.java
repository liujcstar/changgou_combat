package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.changgou.pay.service.AlipayPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayPayServiceImpl implements AlipayPayService {

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public Map nativePay(String orderId, Integer money) {
        //1.创建用于请求的客户端对象
        //2.创建用于接口请求的对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        //3.定义参数
        Map<String,String> map=new HashMap<>();
        System.out.println(orderId);
        map.put("out_trade_no",orderId);
        map.put("total_amount",money+"");
        map.put("subject","畅购");
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。
        // 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。	90m
        map.put("timeout_express","1m");
        request.setBizContent(JSON.toJSONString(map));
        request.setNotifyUrl("http://panhaoxiang.cross.echosite.cn/alipay/notify");//设置回调url
        //4.使用支付宝接口调用的对象发起预下单请求
        AlipayTradePrecreateResponse response =null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
        //5.处理结果
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("qr_code",response.getQrCode());//支付url
        result.put("orderId",response.getOutTradeNo());//订单id
        return result;
    }

    @Override
    public Map queryOrder(String orderId) {
        //1.创建一个用于调用端口的客户端对象
        //2.创建用户请求对象
        AlipayTradeQueryRequest request=new AlipayTradeQueryRequest();
        //3.封装请求参数
        Map<String,String> map=new HashMap<>();
        map.put("out_trade_no",orderId);
        /*  biz_content: 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，
        具体参照各产品快速接入文档*/
        request.setBizContent(JSON.toJSONString(map));
        //4.发起接口请求
        AlipayTradeQueryResponse response= null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //处理结果
        //获取订单的交易状态
        //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
        // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
        // TRADE_SUCCESS（交易交易支付成功支付成功）、
        // TRADE_FINISHED（交易结束，不可退款）
        System.out.println("交易状态:"+response.getTradeStatus());
        System.out.println("订单金额:"+response.getTotalAmount());
        System.out.println("订单号:"+response.getTradeNo());
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("trade_status",response.getTradeStatus());
        result.put("total",response.getTotalAmount());
        result.put("orderId",response.getTradeNo());
        return result;
    }

    @Override
    public Map closeOrder(String orderId) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        Map<String,Object> map=new HashMap<>();
        map.put("out_trade_no",orderId);
        request.setBizContent(JSON.toJSONString(map));
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            //判断订单有没有关闭成功
            String code = response.getBody();
            if ("10000".equals(code)){
                Map<String,Object> result=new HashMap<>();
                result.put("code",code);
                return result;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
