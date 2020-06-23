package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.dao.OrderMapper;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/8 21:44
 */

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Override
    public int addOrder(OrderInfo orderInfo) {
        return orderMapper.addOrder(orderInfo);
    }

    @Override
    public OrderInfo getOrderInfoByOrderId(Integer id) {
        return orderMapper.getOrderInfoByOrderId(id);
    }

    @Override
    public OrderInfo getOrderInfoBySeckillOrderId(Integer id) {
        return orderMapper.getOrderInfoByOrderId(id);
    }

    @Override
    public List<OrderInfo> getAllOrderByUserId(Integer userId) {
        return orderMapper.getAllOrderByUserId(userId);
    }

    @Override
    public void payOrder(Integer orderId) {
        int i = orderMapper.payOrder(orderId);
        if (i == 0) throw new MyException(CodeMsg.PAY_FAIL);
    }

    @Override
    public void setAddress(Integer orderId, Integer addressId) {
        int i = orderMapper.setAddress(orderId, addressId);
        if (i == 0) throw new MyException(CodeMsg.ADDRESS_ERROR);
    }
}
