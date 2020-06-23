package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.dao.GoodsMapper;
import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import com.yiqzq.miaoshasystem.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/7 14:43
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<SeckillGoods> getAllGoods() {
        return goodsMapper.getAllGoods();
    }

    @Override
    public SeckillGoods getSeckillGoodsByGoodsId(int goodsId) {
        return goodsMapper.getseckillGoodsByGoodsId(goodsId);
    }
}
