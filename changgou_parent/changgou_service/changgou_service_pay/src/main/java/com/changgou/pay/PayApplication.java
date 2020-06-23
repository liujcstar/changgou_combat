package com.changgou.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.changgou.pay.config.AlipayConfig;
import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }

    @Bean
    public WXPay wxPay(){
        try {
            return new WXPay(new MyConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Autowired
    private AlipayConfig alipayConfig;

    //将支付宝请求的客户端对象实例化为bean
    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(alipayConfig.getServerUrl()
                ,alipayConfig.getAppId()
                ,alipayConfig.getPrivateKey()
                ,"json"
                ,"utf-8"
                ,alipayConfig.getAliPayPublicKey()
                ,"RSA2");
    }
}
