package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.TestJSR303;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.RRR;
import com.yiqzq.miaoshasystem.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yiqzq
 * @date 2020/6/6 12:50
 */
@Slf4j
@RestController
public class TestController {
    @GetMapping("/test/exception")
    public Result<Object> save(int id, HttpServletRequest request, HttpSession session) throws Exception {
        if (id == 1) throw new MyException(CodeMsg.CAPTCHA_IS_EMPTY);
        else return Result.noMessage();
    }

    @GetMapping("/test/exception2")
    public RRR save(int id) throws Exception {
        return new RRR(1, "RRR");
    }

    @GetMapping("/test/jsr303")
    public String testJsr303(@Validated TestJSR303 entity) {
        log.info("entity is {}", entity);
        return "success";
    }


    @AccessLimit()
    @GetMapping("/test/testAccessLimit")
    @ResponseBody
    public String testAccessLimit(@Validated TestJSR303 entity) {
        return "ok";
    }
}
