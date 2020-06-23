package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.imagecode.ImageCode;
import com.yiqzq.miaoshasystem.imagecode.ImageConstants;
import com.yiqzq.miaoshasystem.pojo.RegisterUser;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.RegisterUserService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yiqzq
 * @date 2020/6/4 23:22
 */
@Controller
public class RegisterController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    RegisterUserService registerUserService;

    @RequestMapping("/user/register")
    @ResponseBody
    public Result<User> register(RegisterUser registerUser, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //获得参数
        ImageCode codeInSession = (ImageCode) session.getAttribute(ImageConstants.SESSION_KEY_CODE);
        String codeInForm = registerUser.getCode();
        String password1 = registerUser.getPassword();
        String password2 = registerUser.getCheckpassword();
        String username = registerUser.getUsername();
        String phone = registerUser.getMobile();
        int length = registerUser.getPasswordLength();
        //检验验证码
        registerUserService.validCaptcha(codeInSession, codeInForm, session);
        //校验密码
        registerUserService.validPassword(password1, password2, length);
        //校验用户名
        registerUserService.validUsername(username);
        //校验手机号
        registerUserService.validPhone(phone);
        //插入用户
        Result<User> user = registerUserService.insertUser(registerUser);
        if (user.isSuccess()) {
            CookieUtil.writeLoginToken(request,response, session.getId());
            RedisUtil.set("user:" + session.getId(), user.getData(), RedisCacheExpire.USER_SESSION);
        }
        return user;
    }


    @RequestMapping("/user_register.html")
    public String user_register() {
        return "user_register";
    }

}
