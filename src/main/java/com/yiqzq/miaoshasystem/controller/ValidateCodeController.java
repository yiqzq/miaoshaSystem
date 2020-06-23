package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.imagecode.ImageCode;
import com.yiqzq.miaoshasystem.imagecode.ImageCodeGenerator;
import com.yiqzq.miaoshasystem.imagecode.ImageConstants;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yiqzq
 * @date 2020/6/4 22:44
 */
@RestController
public class ValidateCodeController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 获取验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("/code/image/**")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = ImageCodeGenerator.createImageCode();
        sessionStrategy.setAttribute(new ServletWebRequest(request), ImageConstants.SESSION_KEY_CODE, imageCode);
        ImageIO.write(imageCode.getImage(), ImageConstants.CODE_TYPE, response.getOutputStream());
    }

    @GetMapping("/code/seckill")
    public void getCode2(HttpServletRequest request, HttpServletResponse response, Integer goodsId) throws IOException {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + loginToken);
        if (user == null) {
            throw new MyException(CodeMsg.USER_NO_LOGIN);
        }
        ImageCode imageCode = ImageCodeGenerator.createImageCode();
        RedisUtil.set("seckillImageCode-userId:" + user.getId() + "-goodsId:" + goodsId, imageCode.getCode(), RedisCacheExpire.SECKILL_IMAGE_CODE);
        ImageIO.write(imageCode.getImage(), ImageConstants.CODE_TYPE, response.getOutputStream());
    }
}