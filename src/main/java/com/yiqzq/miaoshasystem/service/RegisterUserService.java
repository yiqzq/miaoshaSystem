package com.yiqzq.miaoshasystem.service;

import com.yiqzq.miaoshasystem.imagecode.ImageCode;
import com.yiqzq.miaoshasystem.pojo.RegisterUser;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.Result;

import javax.servlet.http.HttpSession;

/**
 * @author yiqzq
 * @date 2020/6/5 17:51
 */
public interface RegisterUserService {
    public void getUserByMobile(String phone);

    public Result<User> insertUser(RegisterUser user);

    public void validCaptcha(ImageCode codeInSession, String codeInForm, HttpSession session);

    public void  validPassword(String password1, String password2,int length);

    public void validUsername(String username);

    public void  validPhone(String phone);
}
