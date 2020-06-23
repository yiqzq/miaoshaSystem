package com.yiqzq.miaoshasystem.dao;

import com.yiqzq.miaoshasystem.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/8 16:57
 */
@Component
@Mapper
public interface OrderMapper {
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);

    OrderInfo insert(User user, SeckillGoods goods);

    int addOrder(OrderInfo orderInfo);

    int addSeckillOrder(SeckillOrder seckillOrder);

    OrderInfo getOrderInfoByOrderId(Integer id);

//    OrderInfo getOrderInfoBySeckillOrderId(Integer id);

    List<OrderInfo> getAllOrderByUserId(Integer userId);

    int payOrder(Integer orderId);

    int setAddress(Integer orderId, Integer addressId);
}
