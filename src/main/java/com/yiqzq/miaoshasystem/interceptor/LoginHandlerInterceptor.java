package com.yiqzq.miaoshasystem.interceptor;

/**
 * @author yiqzq
 * @date 2020/6/6 23:41
 */

import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {
    static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String id = CookieUtil.readLoginToken(request);
        User user = (User) RedisUtil.get("user:" + id);
        if (user == null) {
            response.sendRedirect("/");
            return false;
        } else {
            threadLocal.set(user);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        User user = threadLocal.get();
//        log.info("LoginHandlerInterceptor.afterCompletion:" + user);
        threadLocal.remove();
    }

    public static User getLoginUser() {
        return threadLocal.get();
    }

    public static void setLoginUser(User user) {
        threadLocal.set(user);
    }
}