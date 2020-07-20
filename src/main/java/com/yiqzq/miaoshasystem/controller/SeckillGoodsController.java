package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.mq.MQSender;
import com.yiqzq.miaoshasystem.pojo.*;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.GoodsService;
import com.yiqzq.miaoshasystem.service.SeckillGoodsService;
import com.yiqzq.miaoshasystem.service.SeckillOrderService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/8 13:59
 */
@Controller
@Slf4j
public class SeckillGoodsController implements InitializingBean {
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MQSender mqSender;
    @Autowired
    RedisUtil redisUtil;
    private final HashMap<Integer, Boolean> localOverMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SeckillGoods> goodsList = goodsService.getAllGoods();
        if (goodsList == null) {
            return;
        }
        for (SeckillGoods goods : goodsList) {
            RedisUtil.set("goodsId:" + goods.getId() + "-stock", goods.getStockCount());
            RedisUtil.set("goodsId:" + goods.getId() + "-startDate", goods.getStartDate());
            localOverMap.put(goods.getId(), false);
        }
    }

    @AccessLimit
    @RequestMapping(value = "/seckill/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, User user,
                                         @RequestParam("goodsId") int goodsId) {
        String loginToken = CookieUtil.readLoginToken(request);
        user = (User) RedisUtil.get("user:" + loginToken);
        if (user == null) {
            throw new MyException(CodeMsg.USER_NO_LOGIN);
        }
        String path = seckillOrderService.createMiaoshaPath(user, goodsId);
        log.info("path:" + path);
        return Result.success(path);
    }

    //AccessLimit 限流
    @AccessLimit
    @RequestMapping(value = "/seckill/{path}/seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model,
                                @RequestParam("goodsId") Integer goodsId,
                                @PathVariable("path") String path,
                                String seckillCode,
                                HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + loginToken);
        //验证用户
        if (user == null) throw new MyException(CodeMsg.USER_NO_LOGIN);
        //验证秒杀时间是否到了
        seckillGoodsService.validateTime(goodsId, new Date());
        //验证验证码
        seckillGoodsService.validateCapha(seckillCode, user.getId(), goodsId);
        //验证path
        boolean check = seckillOrderService.checkPath(user, goodsId, path);
        if (!check) {
            throw new MyException(CodeMsg.REQUEST_ILLEGAL);
        }
        log.info("goodsId:" + goodsId);
        //内存标记，减少redis访问
        //验证商品是否被秒杀完了
        Boolean over = localOverMap.get(goodsId);
        if (over) {
            log.info("goodsId:" + goodsId + "over");
            throw new MyException(CodeMsg.SECKILL_OVER);
        }


        //预减库存
        long stock = RedisUtil.decr("goodsId:" + goodsId + "-stock", 1);
        log.info("stock2:" + stock);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            throw new MyException(CodeMsg.SECKILL_OVER);
        }

        //判断是否已经秒杀到了,禁止重复秒杀,2遍
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //入队
        MessageInSeckillqueue message = new MessageInSeckillqueue();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendMsg(message);
        return Result.success(0);//排队中
        //获得秒杀商品
//        SeckillGoods goods = seckillGoodsService.getSeckillGoodsByGoodsId(goodsId);
//        if (goods == null) {
//            return Result.error(CodeMsg.NO_GOODS);
//        }
//
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = seckillOrderService.insert(user, goods);
//        return Result.success(orderInfo);
    }

    /**
     * 客户端轮询查询是否下单成功,放入mq中异步查询
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/seckill/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Integer> miaoshaResult(@RequestParam("goodsId") Integer goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + loginToken);
        if (user == null) {
            throw new MyException(CodeMsg.USER_NO_LOGIN);
        }
        Client client = new Client("139.9.128.222", 6379);
        boolean result = client.exists("result", "" + user.getId() + "-" + goodsId);
        log.info("" + user.getId() + "-" + goodsId + "-" + result);
        if (!result) return Result.success(0);
        Integer answer = seckillOrderService.getSeckillResult(user.getId(), goodsId);
        return Result.success(answer);
    }
/**
 receive:{"goodsId":1,"user":{"id":1,"loginCount":1,"password":"","phone":"18305065625","salt":"sdfsfs","userName":"admin"}}
 */
}
