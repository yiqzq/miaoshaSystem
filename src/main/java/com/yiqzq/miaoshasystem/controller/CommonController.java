package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.interceptor.LoginHandlerInterceptor;
import com.yiqzq.miaoshasystem.pojo.Address;
import com.yiqzq.miaoshasystem.pojo.OrderInfo;
import com.yiqzq.miaoshasystem.pojo.StringUser;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.RegisterUserService;
import com.yiqzq.miaoshasystem.service.UserService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/19 11:34
 */
@RestController
@Slf4j
public class CommonController {
    @Autowired
    UserService userService;
    @Autowired
    RegisterUserService registerUserService;

    @RequestMapping("/getUser")
    public Result<User> getUser(HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        return Result.success(user);
    }

    @RequestMapping("/getUser2")
    public Result<StringUser> getUser2(HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        StringUser suser = new StringUser(user);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(user.getRegisterDate());
        String format2 = format.format(user.getLastLoginDate());
        suser.setRegisterDate(format1);
        suser.setLastLoginDate(format2);
        return Result.success(suser);
    }

    @RequestMapping("/logout")
    public Result<User> logout(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        RedisUtil.delete("user:" + loginToken);
        return Result.success(null);
    }

    @RequestMapping("/allOrderInfo")
    public Result<Object> getOrderPageInfo(HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        List<OrderInfo> allOrder = (List<OrderInfo>) RedisUtil.get("userId:" + user.getId() + "-allOrder");
        if (allOrder != null) return Result.success(allOrder);
        allOrder = userService.getAllOrderByUserId(user.getId());
        RedisUtil.set("userId:" + user.getId() + "-allOrder", allOrder, RedisCacheExpire.ALL_ORDER);
        return Result.success(allOrder);
    }

    @RequestMapping("/allAddress")
    public Result<Object> getAllAddress(HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        List<Address> allAddress = (List<Address>) RedisUtil.get("userId:" + user.getId() + "-allAddress");
        if (allAddress != null) {
//            System.out.println("!=null");
            return Result.success(allAddress);
        }
//        System.out.println("==null");
        allAddress = userService.getAllAddressByUserId(user.getId());
        RedisUtil.set("userId:" + user.getId() + "-allAddress", allAddress, RedisCacheExpire.ALL_ADDRESS);
        return Result.success(allAddress);
    }

    @RequestMapping("/addAddress")
    public Result<Object> AddAddress(String telephone, String address, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        RedisUtil.delete("userId:" + user.getId() + "-allAddress");
        userService.addAddress(user.getId(), telephone, address);
        return Result.success(null);
    }

    @RequestMapping("/deleteAddress")
    public Result<Object> deleteAddress(Integer id, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
//        log.info(id.toString());
        RedisUtil.delete("userId:" + user.getId() + "-allAddress");
        RedisUtil.delete("addressId:" + id);

        userService.deleteAddress(id);
        return Result.success(null);
    }

    @RequestMapping("/updateAddress")
    public Result<Object> updateAddress(Integer id, String telephone, String address, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        RedisUtil.delete("userId:" + user.getId() + "-allAddress");
        RedisUtil.set("addressId:" + id, address, RedisCacheExpire.ADDRESS);
        userService.updateAddress(id, telephone, address);
        return Result.success(null);
    }

    @RequestMapping("/getAddress")
    public Result<Object> getAddress(Integer addressId, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        Address address = (Address) RedisUtil.get("addressId:" + addressId);
        if (address == null) {
            address = userService.getAddress(addressId);
            RedisUtil.set("addressId:" + addressId, address, RedisCacheExpire.ADDRESS);
        }
        return Result.success(address);
    }


}
