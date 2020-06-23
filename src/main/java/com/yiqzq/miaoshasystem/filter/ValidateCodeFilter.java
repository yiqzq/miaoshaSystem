package com.yiqzq.miaoshasystem.filter;

import com.yiqzq.miaoshasystem.imagecode.ImageCode;
import com.yiqzq.miaoshasystem.imagecode.ImageConstants;
import com.yiqzq.miaoshasystem.pojo.RegisterUser;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author yiqzq
 * @date 2020/6/4 23:11
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Getter
    @Setter
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Getter
    @Setter
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/user/register", request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            Result<RegisterUser> result = validate(new ServletWebRequest(request));
            if (result == null) {
                System.out.println("验证成功");
                filterChain.doFilter(request, response);

            } else {
                HttpSession session = request.getSession();
                session.setAttribute("msg", result);
                System.out.println("验证失败");
                request.getRequestDispatcher("/index.html").forward(request, response);
            }
        }

    }

    private Result<RegisterUser> validate(ServletWebRequest request) throws ServletRequestBindingException {
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ImageConstants.SESSION_KEY_CODE);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), ImageConstants.LOGIN_FORM_CODE);
        if (StringUtils.isBlank(codeInRequest)) {
            return Result.error(CodeMsg.CAPTCHA_IS_EMPTY);
        }
        if (codeInSession == null) {
            return Result.error(CodeMsg.CAPTCHA_IS_NULL);
        }
        if (codeInSession.isExpire()) {
            sessionStrategy.removeAttribute(request, ImageConstants.SESSION_KEY_CODE);
            return Result.error(CodeMsg.CAPTCHA_IS_EXPIRE);
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            return Result.error(CodeMsg.CAPTCHA_IS_ERROR);
        }
        sessionStrategy.removeAttribute(request, ImageConstants.SESSION_KEY_CODE);
        return null;
    }
}