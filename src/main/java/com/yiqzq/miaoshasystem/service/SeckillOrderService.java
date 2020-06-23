package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.SeckillOrder;
import com.yiqzq.miaoshasystem.pojo.User;

/**
 * @author yiqzq
 * @date 2020/6/8 14:14
 */
public interface SeckillOrderService {
     String createMiaoshaPath(User user, Integer goodsId);

    boolean checkPath(User user, int goodsId, String path);

    SeckillOrder getSeckillOrderByUserIdAndGoodsId(int id, int goodsId);

    OrderInfo insert(User user, SeckillGoods goods);

    int getSeckillResult(int id, Integer goodsId);

}
