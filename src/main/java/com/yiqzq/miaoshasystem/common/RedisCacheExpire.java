package com.yiqzq.miaoshasystem.common;

/**
 * @author yiqzq
 * @date 2020/6/6 22:41
 */
public interface RedisCacheExpire {
    int USER_SESSION = 60 * 30;//30分钟
    int ALL_ORDER = 60 * 10;//10min
    int ALL_ADDRESS = 60 * 30;//30min
    int GOODS_LIST = 5;//5s
    int GOODS_ID = 60;//1分钟
    int ADDRESS = 30 * 60;//30min
    int SECKILL_PATH = 60;//1分钟
    int GOODS_DETAIL = 60;//1分钟
    int SECKILL_IMAGE_CODE = 300;//5min
}