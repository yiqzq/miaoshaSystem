package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.pojo.Address;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/4 16:05
 */

public interface UserService {
    Result<User> getUserByMobile(String mobile, String password);

    int updateUserImage(User user);

    int updateUserName(User user);

    User updateUserPassword(String password, String newPassword, User user);

    List<OrderInfo> getAllOrderByUserId(Integer userId);

    List<Address> getAllAddressByUserId(Integer userId);

    void addAddress(Integer userId, String telephone, String address);

    void validPhone(String telephone);

    void deleteAddress(Integer id);

    void updateAddress(Integer id, String telephone, String address);

    Address getAddress(Integer addressId);
}
