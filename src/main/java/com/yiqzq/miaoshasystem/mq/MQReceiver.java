package com.yiqzq.miaoshasystem.mq;

import com.yiqzq.miaoshasystem.config.MQConfig;
import com.yiqzq.miaoshasystem.pojo.MessageInSeckillqueue;
import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import com.yiqzq.miaoshasystem.pojo.SeckillOrder;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.service.GoodsService;
import com.yiqzq.miaoshasystem.service.OrderService;
import com.yiqzq.miaoshasystem.service.SeckillGoodsService;
import com.yiqzq.miaoshasystem.service.SeckillOrderService;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yiqzq
 * @date 2020/6/16 20:30
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    OrderService orderService;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    SeckillGoodsService seckillGoodsService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("receive:" + message);
        MessageInSeckillqueue messageInSeckillqueue = RedisUtil.stringToBean(message, MessageInSeckillqueue.class);
        User user = messageInSeckillqueue.getUser();
        Integer goodsId = messageInSeckillqueue.getGoodsId();

        //第二次查询数据库确认是否有库存,在消息队列中处理,单线程,串行?
        SeckillGoods goods = goodsService.getSeckillGoodsByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        //减库存 下订单 写入秒杀订单
        seckillOrderService.insert(user, goods);
    }
}
