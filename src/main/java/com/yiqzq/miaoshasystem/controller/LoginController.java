package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.UserService;
import com.yiqzq.miaoshasystem.service.impl.UserServiceImpl;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yiqzq
 * @date 2020/6/4 14:04
 */

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/user/login")
    @ResponseBody
    public Result<User> toLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response, String mobile, String password) {

        Result<User> result = userService.getUserByMobile(mobile, password);
        if (result.isSuccess()) {
            CookieUtil.writeLoginToken(request,response, session.getId());
            RedisUtil.set("user:" + session.getId(), result.getData(), RedisCacheExpire.USER_SESSION);
        }
        return result;
    }


    @RequestMapping({"/", "/index.html"})
    public String toIndex() {
        return "login";
    }


    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
