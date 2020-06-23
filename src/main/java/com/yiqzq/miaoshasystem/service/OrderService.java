package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.pojo.OrderInfo;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/8 21:44
 */
public interface OrderService {
    int addOrder(OrderInfo orderInfo);

    OrderInfo getOrderInfoByOrderId(Integer id);

    OrderInfo getOrderInfoBySeckillOrderId(Integer id);


    public List<OrderInfo> getAllOrderByUserId(Integer UserId);

    void payOrder(Integer OrderId);

    void setAddress(Integer OrderId, Integer addressId);
}
