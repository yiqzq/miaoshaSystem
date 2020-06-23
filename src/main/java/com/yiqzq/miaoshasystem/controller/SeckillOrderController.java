package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.GoodsService;
import com.yiqzq.miaoshasystem.service.OrderService;
import com.yiqzq.miaoshasystem.service.SeckillGoodsService;
import com.yiqzq.miaoshasystem.service.SeckillOrderService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yiqzq
 * @date 2020/6/9 13:57
 */
@Controller
@Slf4j
public class SeckillOrderController {
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/order/detail")
    @ResponseBody
    public Result<OrderInfo> getOderInfo(@RequestParam("orderId") Integer orderId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + loginToken);
        if (user == null) {
            throw new MyException(CodeMsg.USER_NO_LOGIN);
        }
//        log.info("Order:" + orderId);
        OrderInfo orderInfo = orderService.getOrderInfoByOrderId(orderId);
        if (orderInfo == null) {
            throw new MyException(CodeMsg.ORDER_NOT_EXIST);
        }
        return Result.success(orderInfo);
    }

    @ResponseBody
    @PostMapping("/pay")
    @AccessLimit
    public Result<Object> pay(Integer orderId, Integer addrId, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        RedisUtil.delete("userId:" + user.getId() + "-allOrder");
        orderService.setAddress(orderId, addrId);
        orderService.payOrder(orderId);
        log.info("/pay-通过");
        return Result.success(null);
    }
}
