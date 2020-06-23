package com.yiqzq.miaoshasystem.exception;

import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import io.lettuce.core.RedisCommandTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/6 11:11
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("请求的url为{}出现数据校验异常,异常信息为:{}",
                request.getRequestURI(), e.getClass());
        e.printStackTrace();
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            logger.info(String.valueOf(myException.getCm()));
            return Result.error(myException.getCm());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            List<Object> msg = new ArrayList<>();
            for (ObjectError error : errors) {
                msg.add(error.getDefaultMessage());
            }
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else if (e instanceof RedisCommandTimeoutException) {
            return Result.error(CodeMsg.REDIS_CONNECT_TIME_OUT);
        } else {
            return Result.error(CodeMsg.UNKNOWN_ERROR);
        }
    }

}
