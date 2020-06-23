package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.pojo.SeckillGoods;

import java.util.Date;

/**
 * @author yiqzq
 * @date 2020/6/8 16:31
 */
public interface SeckillGoodsService {
    SeckillGoods getSeckillGoodsByGoodsId(int goodsId);

    int reduceStock(Integer id);

    boolean validateCapha(String seckillCode, Integer userId, Integer goodsId);

    void validateTime(Integer goodsId, Date nowDate);
}
