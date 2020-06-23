package com.yiqzq.miaoshasystem.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import sun.rmi.runtime.Log;

/**
 * @author yiqzq
 * @date 2020/6/4 10:23
 */
@Component
public class MD5Util {

    private static final String SALT = "yiqzq456";

    public static String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public static String inputPasswordToFormPassword(String password) {
        String str1 = ""+SALT.charAt(0) + SALT.charAt(2) + SALT.charAt(4) + password;
        return md5(str1);
    }

    public static String formPasswordToDBPassword(String password, String salt) {
        return md5(""+salt.charAt(0) + salt.charAt(2) + salt.charAt(4) + password);
    }

    public static String inputPasswordToDBPassword(String password, String salt) {
        String str1 = inputPasswordToFormPassword(password);
        return formPasswordToDBPassword(str1, salt);
    }

    /**
     原密码:123456
     一次md5:3e0214ed9475ef53bfaffa8a3c40c115
     二次md5:3a8b42032fac7d56221c4ce51164fbc1
     */
    public static void main(String[] args) {
        String password = "123456";
        String salt = "sdfsfs";
        System.out.println("原密码:" + password);
        System.out.println("一次md5:" + inputPasswordToFormPassword(password));
        System.out.println("二次md5:" + inputPasswordToDBPassword(password, salt));
    }

}
