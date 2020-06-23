package com.changgou.goods.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.CourierData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-19 23:54
 */
@Controller
@CrossOrigin
@RequestMapping("/kuaidi")
public class KuaidiController {

    public static final String AppKey = "46ca3dc3b3c2cf72064fbb278a648d2d";

    private String json = "{\n" +
            "  \"resultcode\": \"200\", /* 老版状态码，新用户请忽略此字段 */\n" +
            "  \"reason\": \"查询物流信息成功\",\n" +
            "  \"result\": {\n" +
            "    \"company\": \"EMS\", /* 快递公司名字 */\n" +
            "    \"com\": \"ems\",\n" +
            "    \"no\": \"1186465887499\", /* 快递单号 */\n" +
            "    \"status\": \"1\", /* 1表示此快递单的物流信息不会发生变化，此时您可缓存下来；0表示有变化的可能性 */,\n" +
            "    \"status_detail\": \"PENDING\", /* 详细的状态信息，可能为null，仅作参考。其中：\n" +
            "        PENDING 待查询\n" +
            "        NO_RECORD 无记录\n" +
            "        ERROR 查询异常\n" +
            "        IN_TRANSIT 运输中\n" +
            "        DELIVERING 派送中\n" +
            "        SIGNED 已签收\n" +
            "        REJECTED 拒签\n" +
            "        PROBLEM 疑难件\n" +
            "        INVALID 无效件\n" +
            "        TIMEOUT 超时件\n" +
            "        FAILED 派送失败\n" +
            "        SEND_BACK 退回\n" +
            "        TAKING 揽件 */\n" +
            "    \"list\": [\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-15 21:44:04\",  /* 物流事件发生的时间 */\n" +
            "        \"remark\": \"离开郴州市 发往长沙市【郴州市】\", /* 物流事件的描述 */\n" +
            "        \"zone\": \"\" /* 快件当时所在区域，由于快递公司升级，现大多数快递不提供此信息 */\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-15 21:46:45\",\n" +
            "        \"remark\": \"郴州市邮政速递物流公司国际快件监管中心已收件（揽投员姓名：侯云,联系电话:）【郴州市】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-16 12:04:00\",\n" +
            "        \"remark\": \"离开长沙市 发往贵阳市（经转）【长沙市】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-17 07:53:00\",\n" +
            "        \"remark\": \"到达贵阳市处理中心（经转）【贵阳市】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-18 07:40:00\",\n" +
            "        \"remark\": \"离开贵阳市 发往毕节地区（经转）【贵阳市】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-18 09:59:00\",\n" +
            "        \"remark\": \"离开贵阳市 发往下一城市（经转）【贵阳市】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-18 12:01:00\",\n" +
            "        \"remark\": \"到达  纳雍县 处理中心【毕节地区】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-18 17:34:00\",\n" +
            "        \"remark\": \"离开纳雍县 发往纳雍县阳长邮政支局【毕节地区】\",\n" +
            "        \"zone\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"datetime\": \"2016-06-20 17:55:00\",\n" +
            "        \"remark\": \"投递并签收，签收人：单位收发章 *【毕节地区】\",\n" +
            "        \"zone\": \"\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"error_code\": 0 /* 错误码，0表示查询正常，其他表示查询不到物流信息或发生了其他错误 */\n" +
            "}";


    @GetMapping(value = {"/find/{com}/{no}/{receiverPhone}", "/find/{com}/{no}"})
    @ResponseBody
    public Result find(@PathVariable("com") String com, @PathVariable("no") String no, @PathVariable(required = false, value = "receiverPhone") String receiverPhone) throws IOException {



        String url = "";
        // 请求的url地址
        if (StringUtils.isNotEmpty(receiverPhone) && receiverPhone.length() == 11) {
//            截取收货人的手机号后四位 （查询顺丰快递需要 ）
            String subReceiverPhone = receiverPhone.substring(7);
            url = "http://v.juhe.cn/exp/index?com=" + com + "&no=" + no + "&receiverPhone=" + subReceiverPhone + "&key=" + AppKey;

        } else {
            url = "http://v.juhe.cn/exp/index?com=" + com + "&no=" + no + "&key=" + AppKey;
        }

        //创建okHttpClient对象
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            String message = response.body().string();

//            测试数据
//            String message = json;
            Map map = JSON.parseObject(message, Map.class);

            JSONObject obj = JSONObject.parseObject(message);

            if ("0".equals(obj.getString("error_code"))) {
//                获取数据成功
                String result = obj.getString("result");
                JSONObject list = JSONObject.parseObject(result);
                String listJson = list.getString("list");
                List<CourierData> data = JSON.parseObject(listJson, List.class);

//            倒序
                Collections.reverse(data);

                System.out.println(data);

                return new Result(true, StatusCode.OK, "请求发送成功", data);

            } else {
//                获取数据失败
                return new Result(false, StatusCode.ERROR, "获取数据失败，请检查快递公司和单号是否正确");

            }

        }

        return new Result(false, StatusCode.ERROR, "请求发送失败");
    }


    @GetMapping(value = {"/findDetail/{com}/{no}/{receiverPhone}", "/findDetail/{com}/{no}"})
    public String findDetail(@PathVariable("com") String com, @PathVariable("no") String no, @PathVariable(required = false, value = "receiverPhone") String receiverPhone, Model model) throws IOException {

//        如果是公司名就转为公司代号 否则不做改变
        com = this.companyToCode(com);

        String url = "";
        // 请求的url地址
        if (StringUtils.isNotEmpty(receiverPhone) && receiverPhone.length() == 11) {
//            截取收货人的手机号后四位 （查询顺丰快递需要 ）
            String subReceiverPhone = receiverPhone.substring(7);
            url = "http://v.juhe.cn/exp/index?com=" + com + "&no=" + no + "&receiverPhone=" + subReceiverPhone + "&key=" + AppKey;

        } else {
            url = "http://v.juhe.cn/exp/index?com=" + com + "&no=" + no + "&key=" + AppKey;
        }

        //创建okHttpClient对象
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            String message = response.body().string();

//            测试数据
//            String message = json;
            Map map = JSON.parseObject(message, Map.class);

            JSONObject obj = JSONObject.parseObject(message);

            if ("0".equals(obj.getString("error_code"))) {
//                获取数据成功
                String result = obj.getString("result");
                JSONObject list = JSONObject.parseObject(result);
                String listJson = list.getString("list");
                List<CourierData> data = JSON.parseObject(listJson, List.class);

//            倒序
                Collections.reverse(data);

                System.out.println(data);

                model.addAttribute("list", data);
                model.addAttribute("flag", true);
                model.addAttribute("name", this.getCompany(com));
                model.addAttribute("number", no);

                return "detail1";

            } else {
//                获取数据失败
                model.addAttribute("flag", false);
                model.addAttribute("list", "");

                return "detail1";

            }

        }
        model.addAttribute("flag", false);
        model.addAttribute("list", "");
        return "detail1";
    }


    @RequestMapping("/show")
    public String show() {
        return "showview";
    }

    @RequestMapping("/map")
    public String map() {
        return "map";
    }



//    快递名转换  代号转名称
    public String getCompany(String str){
        if(StringUtils.isNotEmpty(str)){
            if("sf".equals(str)){
                return "顺丰快递";
            }
            if("sto".equals(str)){
                return "申通快递";
            }
            if("yt".equals(str)){
                return "圆通快递";
            }
            if("yd".equals(str)){
                return "韵达快递";
            }
            if("tt".equals(str)){
                return "天天快递";
            }
            if("zto".equals(str)){
                return "中通快递";
            }
            if("ems".equals(str)){
                return "邮政快递";
            }
            if("qf".equals(str)){
                return "全峰快递";
            }
            if("ht".equals(str)){
                return "百世快递";
            }
            if("zjs".equals(str)){
                return "宅急送快递";
            }

        }else {
            return "";
        }

        return "";
    }


    //    快递名转换  名称转代号
    public String  companyToCode(String company){
        if ("顺丰快递".equals(company)){
            return "sf";
        }
        if ("申通快递".equals(company)){
            return "sto";
        }
        if ("圆通快递".equals(company)){
            return "yt";
        }
        if ("韵达快递".equals(company)){
            return "yd";
        }
        if ("天天快递".equals(company)){
            return "tt";
        }
        if ("中通快递".equals(company)){
            return "zto";
        }
        if ("邮政快递".equals(company)){
            return "ems";
        }
        if ("全峰快递".equals(company)){
            return "qf";
        }
        if ("百世快递".equals(company)){
            return "ht";
        }
        if ("宅急送快递".equals(company)){
            return "zjs";
        }

        return company;

    }



}

