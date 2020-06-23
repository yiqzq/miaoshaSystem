package com.yiqzq.miaoshasystem.interceptor;

import com.alibaba.fastjson.JSON;
import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.rmi.runtime.Log;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author yiqzq
 * @date 2020/6/18 17:01
 */
@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int limit = accessLimit.limit();
            int sec = accessLimit.sec();
            User u = LoginHandlerInterceptor.getLoginUser();
            String key = "limit-UserId:" + u.getId() + "-uri:" + request.getRequestURI();
            Integer cnt = (Integer) RedisUtil.get(key);

            if (cnt == null) {
                //set时一定要加过期时间
                RedisUtil.set(key, 1, sec);
            } else if (cnt < limit) {
                RedisUtil.incr(key, 1);

            } else {
                output(response, CodeMsg.REQUEST_FREQUENT);
                return false;
            }
        }
        return true;
    }

    public void output(HttpServletResponse response, CodeMsg msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = null;
        String str = JSON.toJSONString(Result.error(msg));
        try {
            out = response.getOutputStream();
            out.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }


}