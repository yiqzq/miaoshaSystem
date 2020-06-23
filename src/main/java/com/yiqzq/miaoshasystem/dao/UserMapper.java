package com.yiqzq.miaoshasystem.dao;

import com.yiqzq.miaoshasystem.pojo.Address;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/4 16:01
 */
@Component
@Mapper
public interface UserMapper {
    @Select("select * from user where phone =#{phone}")
    User getUserByMobile(@Param("phone") String phone);

    int updateUserImage(User user);

    int updateUserUserName(User user);

    int updateUserLoginTimeAndCount(User user);

    int updateUserPassword(User user);

    List<Address> getAllAddressByUserId(Integer userId);

    int addAddress(Integer userId, String telephone, String address);

    int deleteAddress(Integer id);

    void updateAddress(Integer id, String telephone, String address);
    @Select("select * from address where id =#{addressId}")
    Address getAddress(Integer addressId);
}
