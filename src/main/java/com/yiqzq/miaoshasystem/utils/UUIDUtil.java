package com.yiqzq.miaoshasystem.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author yiqzq
 * @date 2020/6/5 18:07
 */
@Component
public class UUIDUtil {
    public static String getRandomSalt() {
        UUID uuid = UUID.randomUUID();
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }
}
