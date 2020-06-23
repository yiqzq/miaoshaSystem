package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.dao.UserMapper;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.Address;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.OrderService;
import com.yiqzq.miaoshasystem.service.UserService;
import com.yiqzq.miaoshasystem.utils.MD5Util;
import com.yiqzq.miaoshasystem.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yiqzq
 * @date 2020/6/4 16:09
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderService orderService;


    @Override
    public Address getAddress(Integer addressId) {
       return userMapper.getAddress(addressId);
    }

    @Override
    public Result<User> getUserByMobile(String mobile, String password) {
        User user = userMapper.getUserByMobile(mobile);
        if (user == null) {
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        addLoginInfo(user);
        String salt = user.getSalt();
        String dbPassword = user.getPassword();
        String inputPassword = MD5Util.formPasswordToDBPassword(password, salt);
        if (dbPassword.equals(inputPassword)) {
            return Result.success(user);
        } else {
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
    }

    public void addLoginInfo(User user) {
        Date date = new Date();
        user.setLastLoginDate(date);
        user.setLoginCount(user.getLoginCount() + 1);
        int i = userMapper.updateUserLoginTimeAndCount(user);
        if (i == 0) throw new MyException(CodeMsg.USERINFO_UPDATE_ERROR);

    }


    @Override
    public int updateUserImage(User user) {
        return userMapper.updateUserImage(user);
    }

    @Override
    public int updateUserName(User user) {
        return userMapper.updateUserUserName(user);
    }

    @Override
    public User updateUserPassword(String password, String newPassword, User user) {
        String salt = user.getSalt();
        String md5Password = getMD5Password(password, salt);
//        logger.info("一次加密:"+password);
//        logger.info("二次次加密:"+md5Password);
//        logger.info("Redis密码:"+user.getPassword());
        if (!StringUtils.equals(md5Password, user.getPassword())) throw new MyException(CodeMsg.PASSWORD_ERROR);
        String newmd5Password = getMD5Password(newPassword, salt);
        user.setPassword(newmd5Password);
        int i = userMapper.updateUserPassword(user);
        if (i == 0) throw new MyException(CodeMsg.USERINFO_UPDATE_ERROR);
        return user;
    }

    @Override
    public List<OrderInfo> getAllOrderByUserId(Integer userId) {
        return orderService.getAllOrderByUserId(userId);
    }

    @Override
    public List<Address> getAllAddressByUserId(Integer userId) {
        return userMapper.getAllAddressByUserId(userId);
    }

    @Override
    public void addAddress(Integer userId, String telephone, String address) {
        validPhone(telephone);
        validAddress(address);
        int i = userMapper.addAddress(userId, telephone, address);
        if (i == 0) throw new MyException(CodeMsg.ADDRESS_ERROR);

    }

    @Override
    public void validPhone(String telephone) {
        Pattern pattern = Pattern.compile("\\d{11}");
        if (telephone.length() != 11 || !pattern.matcher(telephone).matches())
            throw new MyException(CodeMsg.MOBILE_FORMAT_ERROR);
    }

    @Override
    public void deleteAddress(Integer id) {
        int i = userMapper.deleteAddress(id);
        if (i == 0) throw new MyException(CodeMsg.ADDRESS_ERROR);

    }

    @Override
    public void updateAddress(Integer id, String telephone, String address) {
        validPhone(telephone);
        validAddress(address);
        userMapper.updateAddress(id, telephone, address);
    }

    public static String getMD5Password(String password, String salt) {
        return MD5Util.formPasswordToDBPassword(password, salt);
    }

    public void validAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            throw new MyException(CodeMsg.ADDRESS_IS_EMPTY);
        }
    }
}
