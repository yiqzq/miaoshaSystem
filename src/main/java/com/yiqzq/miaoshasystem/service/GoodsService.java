package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.pojo.SeckillGoods;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/7 14:43
 */

public interface GoodsService {
    List<SeckillGoods> getAllGoods();
    SeckillGoods getSeckillGoodsByGoodsId(int goodsId);
}
