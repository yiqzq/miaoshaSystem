package com.yiqzq.miaoshasystem;

import com.yiqzq.miaoshasystem.dao.GoodsMapper;
import com.yiqzq.miaoshasystem.dao.OrderMapper;
import com.yiqzq.miaoshasystem.dao.RegisterUserMapper;
import com.yiqzq.miaoshasystem.dao.UserMapper;
import com.yiqzq.miaoshasystem.mq.MQSender;
import com.yiqzq.miaoshasystem.pojo.*;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import com.yiqzq.miaoshasystem.utils.UUIDUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

@SpringBootTest
class MiaoshasystemApplicationTests {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RegisterUserMapper registerUserMapper;
    @Autowired
    SqlSession sqlSession;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    MQSender mqSender;

    @Test
    void testuuid() {
        System.out.println(UUIDUtil.getRandomSalt());
    }

    @Test
    void contextLoads() {
        System.out.println(registerUserMapper);
        System.out.println(sqlSession);
        User user = new User(231, "asd", "12312312", "asdasd", "asdasd", "", 1, new Date(), new Date());
        User userBymobile = registerUserMapper.getUserByMobile("12312789");
        System.out.println(userBymobile);
    }

    @Test
    void testRedisUtil() {
        User user = new User(231, "asd", "123asdasda12312", "asdasd", "asdasd", "", 1, new Date(), new Date());
        RedisUtil.set("user01", user);
        User user01 = (User) RedisUtil.get("user01");
        user01.setId(111);
        System.out.println(user01);
    }

    @Test
    void testAllGoods() {
        List<SeckillGoods> goods = goodsMapper.getAllGoods();
        System.out.println(goods);
    }

    @Test
    void testgetGoodsByGoodsId() {
        SeckillGoods seckillGoods = goodsMapper.getseckillGoodsByGoodsId(4);
        System.out.println(seckillGoods);
    }

    @Test
    void testGetSkillOrderByUserIdAndGoodsId() {
        SeckillOrder seckillOrder = orderMapper.getSeckillOrderByUserIdAndGoodsId(1, 2);
        System.out.println(seckillOrder);
    }

    @Test
    void testReduceGoods() {
        int i = goodsMapper.reduceStock(1);
        System.out.println(i);
    }

    @Test
    void testAddOrder() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setAddrId(0);
        orderInfo.setGoodsCount(1);
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        int i = orderMapper.addOrder(orderInfo);
        System.out.println(i);
    }

    @Test
    void testAddSeckillOrder() {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(100);
        seckillOrder.setOrderId(100);
        seckillOrder.setUserId(100);
        int i = orderMapper.addSeckillOrder(seckillOrder);
        System.out.println(i);
    }

    @Test
    void testRedis() {
        boolean set = RedisUtil.set("1", "2");
        System.out.println(set);
    }

    @Test
    void testRedisExpire() {
        Boolean exist = RedisUtil.exist("2");
        System.out.println(exist);
    }

    @Test
    void testGetOrderInfoByOrderId() {
        OrderInfo info = orderMapper.getOrderInfoByOrderId(1);
        System.out.println(info);
    }



    @Test
    void testupdateUser() {
        User user = new User();
        user.setUserName("eee");
        user.setId(1);
        user.setHead("F:\\image\\ycy2.jpg");
        int i = userMapper.updateUserImage(user);
        System.out.println(i);
    }

    @Test
    void testgetAllOrderByUserId() {
        List<OrderInfo> allOrderByUserId = orderMapper.getAllOrderByUserId(1);
        System.out.println(allOrderByUserId);
    }

    @Test
    void testgetAllAddressByUserId() {
        List<Address> addresses = userMapper.getAllAddressByUserId(1);
        System.out.println(addresses);
    }

    @Test
    void testaddaddress() {
        int i = userMapper.addAddress(1, "18305065626", "北京");
        System.out.println(i);
    }

    @Test
    void testdeleteaddress() {
        int i = userMapper.deleteAddress(20);
        System.out.println(i);
    }

    @Test
    void testupdateAddress() {
        userMapper.updateAddress(25, "18305065625", "清华");
    }

 @Test
    void testpayOrder() {
      orderMapper.payOrder(467);
    }
    @Test
    void testsetAddress() {
      orderMapper.setAddress(467,27);
    }

}
