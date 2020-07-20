package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.dao.OrderMapper;
import com.yiqzq.miaoshasystem.pojo.*;
import com.yiqzq.miaoshasystem.service.SeckillGoodsService;
import com.yiqzq.miaoshasystem.service.SeckillOrderService;
import com.yiqzq.miaoshasystem.utils.MD5Util;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author yiqzq
 * @date 2020/6/8 14:15
 */
@Slf4j
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Override
    public String createMiaoshaPath(User user, Integer goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUID.randomUUID() + "123456");
        RedisUtil.set("path-userId:" + user.getId() + "-goodsId:" + goodsId, str, RedisCacheExpire.GOODS_ID);
        return str;
    }

    @Override
    public boolean checkPath(User user, int goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathInRedis = (String) RedisUtil.get("path-userId:" + user.getId() + "-goodsId:" + goodsId);
        return path.equals(pathInRedis);
    }

    @Override
    public SeckillOrder getSeckillOrderByUserIdAndGoodsId(int id, int goodsId) {
        return (SeckillOrder) RedisUtil.get("seckillorderuidgid:" + id + "-" + goodsId);
    }

    @Transactional
    @Override
    public OrderInfo insert(User user, SeckillGoods goods) {
        //秒杀商品库存减一
        int success = seckillGoodsService.reduceStock(goods.getId());
        if (success == 1) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCreateDate(new Date());
            orderInfo.setAddrId(0);
            orderInfo.setGoodsCount(1);
            orderInfo.setGoodsId(goods.getId());
            orderInfo.setGoodsName(goods.getGoodsName());
            orderInfo.setGoodsImg(goods.getGoodsImg());
            orderInfo.setGoodsPrice(goods.getSeckillPrice());
            orderInfo.setOrderChannel(1);
            orderInfo.setStatus(0);
            orderInfo.setUserId(user.getId());
            //添加信息进订单
            orderMapper.addOrder(orderInfo);
//            log.info(""+orderInfo.getId());
//            log.info("orderId:"+orderId);
//            log.info("orderId -->" + orderId + "");
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setGoodsId(goods.getId());
            seckillOrder.setOrderId(orderInfo.getId());
            seckillOrder.setUserId(user.getId());
            //插入秒杀表
            orderMapper.addSeckillOrder(seckillOrder);
            //同时存入Redis消息,防止重复秒杀
            RedisUtil.set("seckillorderuidgid:" + user.getId() + "-" + goods.getId(), seckillOrder);
            RedisUtil.delete("userId:" + user.getId() + "-allOrder");
            Client client = new Client("139.9.128.222", 6379);
            client.add("result", "" + user.getId() + "-" + goods.getId());
            return orderInfo;
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public int getSeckillResult(int userId, Integer goodsId) {

        SeckillOrder order = orderMapper.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
        log.info("SeckillOrder:" + order);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(Integer goodsId) {
        return RedisUtil.exist("goodsover:" + goodsId);
    }

    private void setGoodsOver(Integer goodsId) {
        RedisUtil.set("goodsover:" + goodsId, true);
    }
}
