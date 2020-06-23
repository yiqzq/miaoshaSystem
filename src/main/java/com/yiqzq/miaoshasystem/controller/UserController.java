package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.interceptor.LoginHandlerInterceptor;
import com.yiqzq.miaoshasystem.pojo.RegisterUser;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.FileService;
import com.yiqzq.miaoshasystem.service.RegisterUserService;
import com.yiqzq.miaoshasystem.service.UserService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.model.PreferredConstructorDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yiqzq
 * @date 2020/6/19 18:37
 */
@Controller
@Slf4j
public class UserController {
    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;
    @Autowired
    RegisterUserService registerUserService;
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    @AccessLimit
    @PostMapping("/user/update/image")
    @ResponseBody
    public Result<Object> updaterUserImage(MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) throw new MyException(CodeMsg.FILE_IS_EMPTY);
        String path = fileService.validateImage(file);
        if (path == null) throw new MyException(CodeMsg.FILE_UPLOAD_ERROR);
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        user.setHead(path);
        int i = userService.updateUserImage(user);
        if (i == 0) throw new MyException(CodeMsg.FILE_UPLOAD_ERROR);
        //修改Redis缓存中的东西
        RedisUtil.set("user:" + token, user, RedisCacheExpire.USER_SESSION);
        LoginHandlerInterceptor.setLoginUser(user);
        return Result.success(path);
    }

    @AccessLimit
    @PostMapping("/user/update/username")
    @ResponseBody
    public Result<Object> updaterUserUserName(String userName, HttpServletRequest request) {
        registerUserService.validUsername(userName);
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        user.setUserName(userName);
        int i = userService.updateUserName(user);
        if (i == 0) throw new MyException(CodeMsg.USERNAME_UPDATE_ERROR);
        RedisUtil.set("user:" + token, user, RedisCacheExpire.USER_SESSION);
        LoginHandlerInterceptor.setLoginUser(user);
        return Result.success(userName);
    }

    @AccessLimit
    @PostMapping("/user/update/password")
    @ResponseBody
    public Result<User> updaterUserUserName(@RequestParam("password") String password, @RequestParam("newpassword") String newPassword, HttpServletRequest request) {
        String token = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + token);
        user = userService.updateUserPassword(password, newPassword, user);
        RedisUtil.set("user:" + token, user, RedisCacheExpire.USER_SESSION);
        return Result.success(user);
    }
}
