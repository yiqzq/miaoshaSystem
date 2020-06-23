package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.dao.RegisterUserMapper;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.imagecode.ImageCode;
import com.yiqzq.miaoshasystem.imagecode.ImageConstants;
import com.yiqzq.miaoshasystem.pojo.RegisterUser;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.RegisterUserService;
import com.yiqzq.miaoshasystem.utils.MD5Util;
import com.yiqzq.miaoshasystem.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author yiqzq
 * @date 2020/6/5 17:51
 */
@Service
public class RegisterUserServiceImpl implements RegisterUserService {
    @Autowired
    RegisterUserMapper mapper;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void validPhone(String phone) {
        Pattern pattern = Pattern.compile("\\d{11}");
        getUserByMobile(phone);
        if (phone.length() != 11 || !pattern.matcher(phone).matches())
            throw new MyException(CodeMsg.MOBILE_FORMAT_ERROR);
    }

    @Override
    public void validUsername(String username) {
        if (username.length() < 3 || username.length() > 10) throw new MyException(CodeMsg.USERNAME_LENGTH_ERROR);
    }

    @Override
    public void getUserByMobile(String phone) {
        User user = mapper.getUserByMobile(phone);
        if (user != null) {
            throw new MyException(CodeMsg.MOBILE_IS_EXIST);
        }
    }

    @Override
    public Result<User> insertUser(RegisterUser registerUser) {
        String salt = UUIDUtil.getRandomSalt();
        String pwd = registerUser.getPassword();
        registerUser.setPassword(getMD5Password(pwd, salt));
        User user = User.mergeWithRegisterUser(registerUser, salt);
        user.setRegisterDate(new Date());
        user.setHead("/userimg/img.jpg");
        int num = mapper.insertUser(user);
        if (num == 0) {
            throw new MyException(CodeMsg.DATE_ERROR);
        } else {

            return Result.success(user);
        }
    }

    @Override
    public void validPassword(String password1, String password2, int length) {
        if (length < 6 || length > 16) throw new MyException(CodeMsg.PASSWORD_LENGTH_ERROR);
        else if (!password1.equals(password2)) throw new MyException(CodeMsg.PASSWORD_NOT_CONSISTENCY);

    }

    @Override
    public void validCaptcha(ImageCode codeInSession, String codeInForm, HttpSession session) {
        logger.info("codeInSeesion:" + codeInSession);
        logger.info("codeInForm:" + codeInForm);
        if (StringUtils.isBlank(codeInForm)) {
            throw new MyException(CodeMsg.CAPTCHA_IS_EMPTY);
        }
        if (codeInSession == null) {
            throw new MyException(CodeMsg.CAPTCHA_IS_NULL);
        }
        if (codeInSession.isExpire()) {
            session.removeAttribute(ImageConstants.SESSION_KEY_CODE);
            throw new MyException(CodeMsg.CAPTCHA_IS_EXPIRE);
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInForm)) {
            throw new MyException(CodeMsg.CAPTCHA_IS_ERROR);
        }
    }

    public static String getMD5Password(String password, String salt) {
        return MD5Util.formPasswordToDBPassword(password, salt);
    }
}
