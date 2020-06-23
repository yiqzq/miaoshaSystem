package com.yiqzq.miaoshasystem.dao;

import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/7 13:50
 */
@Mapper
@Component
public interface GoodsMapper {
    List<SeckillGoods> getAllGoods();

    SeckillGoods getseckillGoodsByGoodsId(int goodsId);


    int reduceStock(@Param("goodsId") Integer goodsId);
}
