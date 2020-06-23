package com.changgou.web.order.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import com.netflix.discovery.converters.Auto;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worder")
public class OrderController {

    @Autowired
    private AddressFeign addressFeign;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private OrderFeign orderFeign;


    @RequestMapping("/ready/order")
    public String readyOrder(Model model) {
        //收件人的地址信息
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address", addressList);

        //购物车信息
        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");

        model.addAttribute("carts", orderItemList);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalNum", totalNum);

        //默认收件人信息
        for (Address address : addressList) {
            if ("1".equals(address.getIs_default())) {
                //默认收件人
                model.addAttribute("deAddr", address);
                break;

            }
        }
        return "order";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order,String id) {
        Result result = orderFeign.add(order,id);
        return result;
    }

    @GetMapping("/toPayPage")
    public String toPayPage(String orderId, Model model) {
        //获取到订单的相关信息
        Order order = orderFeign.findById(orderId).getData();
        model.addAttribute("orderId", orderId);
        model.addAttribute("payMoney", order.getPayMoney());
        return "pay";
    }


    /**
     * 跳转我的订单页面
     *
     * @return
     */
    @RequestMapping("/toCenterIndex")
    public String toCenterIndex() {
        return "center-index";
    }

    @GetMapping("/centerIndex")
    @ResponseBody
    public Map<String, Object> orderList() {
        //获取到订单的相关信息
        List<Order> orderList = orderFeign.myOrderList().getData();
        String username = null;
        for (Order order : orderList) {
            username = order.getUsername();
            break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("orderList", orderList);
        return map;
    }

    /**
     * 跳转待付款页面
     *
     * @return
     */
    @RequestMapping("/toCenterOrderPay")
    public String toCenterOrderPay() {
        return "center-order-pay";
    }

    @GetMapping("/centerOrderPay")
    @ResponseBody
    public Map<String, Object> orderPayList() {
        //获取到订单的相关信息
        List<Order> orderList = orderFeign.myOrderList().getData();
        List<Order> orders = new ArrayList<>();
        String username = null;
        for (Order order : orderList) {
            username = order.getUsername();
            String orderStatus = order.getOrderStatus();
            if ("0".equals(orderStatus)) {
                orders.add(order);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("orderList", orders);
        return map;
    }

    /**
     * 跳转待发货页面
     * @return
     */
    @RequestMapping("/toCenterOrderSend")
    public String toCenterOrderSend() {
        return "center-order-send";
    }

    @GetMapping("/centerOrderSend")
    @ResponseBody
    public Map<String, Object> orderSendList() {
        //获取到订单的相关信息
        List<Order> orderList = orderFeign.myOrderList().getData();

        List<Order> orders = new ArrayList<>();
        String username = null;
        for (Order order : orderList) {
            username = order.getUsername();
            String orderStatus = order.getOrderStatus();
            if ("1".equals(orderStatus)) {
                orders.add(order);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("orderList", orders);
        return map;
    }

    /**
     * 待收货页面
     * @param
     * @return
     */
    @RequestMapping("/toCenterOrderReceive")
    public String toCenterOrderReceive() {
        return "center-order-receive";
    }

    @GetMapping("/centerOrderReceive")
    @ResponseBody
    public Map<String, Object> orderReceiveList() {
        //获取到订单的相关信息
        List<Order> orderList = orderFeign.myOrderList().getData();
        List<Order> orders = new ArrayList<>();
        String username = null;
        for (Order order : orderList) {
            username = order.getUsername();
            String orderStatus = order.getOrderStatus();
            if ("2".equals(orderStatus)) {
                orders.add(order);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("orderList", orders);
        return map;
    }

    /**
     * 跳转待评价页面
     * @return
     */
    @RequestMapping("/toCenterOrderEvaluate")
    public String toCenterOrderEvaluate() {
        return "center-order-evaluate";
    }

    @GetMapping("/centerOrderEvaluate")
    @ResponseBody
    public Map<String, Object> orderEvaluateList() {
        //获取到订单的相关信息
        List<Order> orderList = orderFeign.myOrderList().getData();
        List<Order> orders = new ArrayList<>();
        String username = null;
        for (Order order : orderList) {
            username = order.getUsername();
            String orderStatus = order.getOrderStatus();
            if ("3".equals(orderStatus)) {
                orders.add(order);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("orderList", orders);
        return map;
    }



    /**
     * 取消订单
     * @param id
     * @param num
     * @return
     */
    @GetMapping("/cancelOrder")
    @ResponseBody
    public Map<String, Object> cancelOrder(String id, Integer num) {
        orderFeign.cancelOrder(id, num);
        Map<String, Object> map = this.orderList();
        return map;
    }
    /**
     * 订单后台分页查询和条件查询
     * @param searchMap
     * @param
     * @param
     * @return
     */
    @PostMapping("/findPage2/search2")
    @ResponseBody
    public PageResult finfPage2(@RequestBody Map searchMap){
        return orderFeign.findPage2(searchMap);

    }
    /**
     * 跳转到后台订单页面
     * @param
     * @return
     */
    @GetMapping("/tohoutai")
    public String list2(){
        return "mange";
    }


    /**
     * POI报表导出
     * @param param
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/exportExcal")
    @ResponseBody
    public Result exportExcal(@RequestBody List<Order> param, HttpServletRequest request, HttpServletResponse response){
        try {
            String path = OrderController.class.getClass().getResource("/template/report_template.xlsx").getPath();
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook execl = new XSSFWorkbook(new FileInputStream(new File(path)));
            //读取第一个工作表
            XSSFSheet sheet = execl.getSheetAt(0);
            int rowNum = 1;
            for (Order orderManage : param) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(orderManage.getId());//订单编号
                row.createCell(1).setCellValue(orderManage.getUsername());//用户账户
                row.createCell(2).setCellValue(orderManage.getReceiverContact());//收货人
                row.createCell(3).setCellValue(orderManage.getReceiverMobile());//收货人手机号
                row.createCell(4).setCellValue(orderManage.getPayMoney());//支付金额
                row.createCell(5).setCellValue(orderManage.getPayType());//支付方式
                row.createCell(6).setCellValue(orderManage.getSourceType());//订单来源
                row.createCell(7).setCellValue(orderManage.getOrderStatus());//订单状态
            }
            //使用输出流进行表格下载,基于浏览器作为客户端下载
            //response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            OutputStream outputStream=response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            execl.write(outputStream);
            outputStream.flush();
            outputStream.close();
            execl.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR,"导出失败");
        }
    }




    @GetMapping(value = {"/kinggm/{time}","/kinggm"})
    @ResponseBody
    public Result kinggm(@PathVariable(required = false,value = "time") String time){
        Result result = orderFeign.kinggm(time);
        return result;
    }

    @GetMapping("showChart")
    public String showChart(){
        return "kinggm1";
    }


    //直接显示
    @GetMapping("/lookOrderStatus")
    @ResponseBody
    public Result lookOrderStatus(){

        Result result = orderFeign.lookOrderStatus();

        System.out.println(result);

        return result;

    }

    @GetMapping("/lookOrderStatus1")
    public String lookOrderStatus1(){

        return "order_statistics";
    }




    /**
     * 再次购买&立即购买
     * @param id
     * @return
     */
    @RequestMapping("/buyOrder")
    public String buyOrder(String id, Model model) {

        //根据id获取到相关订单的信息
        Order order = orderFeign.findById(id).getData();
        //封装数据返回到前台
        Integer totalNum = order.getTotalNum();
        Integer totalMoney = order.getTotalMoney();

        List<Order> orderList = orderFeign.myOrderList().getData();
        for (Order order1 : orderList) {
            if (id.equals(order1.getId())){
                order.setOrderItems(order1.getOrderItems());
            }
        }
        List<OrderItem> orderItems = order.getOrderItems();
        model.addAttribute("carts", orderItems);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("orderId",id);
        //收件人的地址信息
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address", addressList);
        //默认收件人信息
        for (Address address : addressList) {
            if ("1".equals(address.getIs_default())) {
                //默认收件人
                model.addAttribute("deAddr", address);
                break;
            }
        }
        return "order";
    }

    /**
     * 手动确认收货
     * @return
     */
    @GetMapping("/confirmReceive")
    @ResponseBody
    public Map<String, Object> confirmReceive(@RequestParam("id") String id){
        orderFeign.confirmTask(id);
        Map<String, Object> map = this.orderList();
        return map;
    }
}
