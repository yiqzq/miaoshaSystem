package com.yiqzq.miaoshasystem.service.impl;

import com.yiqzq.miaoshasystem.dao.GoodsMapper;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.service.SeckillGoodsService;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yiqzq
 * @date 2020/6/8 16:33
 */
@Slf4j
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public SeckillGoods getSeckillGoodsByGoodsId(int goodsId) {
        return goodsMapper.getseckillGoodsByGoodsId(goodsId);
    }

    @Override
    public int reduceStock(Integer id) {
        return goodsMapper.reduceStock(id);
    }

    @Override
    public boolean validateCapha(String seckillCode, Integer userid, Integer goodsId) {
        String redisCode = (String) RedisUtil.get("seckillImageCode-userId:" + userid + "-goodsId:" + goodsId);
        log.info("redisCode:" + redisCode);
        log.info("seckillCode:" + seckillCode);
        if (StringUtils.isEmpty(redisCode)) throw new MyException(CodeMsg.CAPTCHA_IS_EXPIRE);
        if (StringUtils.isEmpty(seckillCode)) throw new MyException(CodeMsg.CAPTCHA_IS_EMPTY);
        if (!StringUtils.equals(redisCode, seckillCode)) throw new MyException(CodeMsg.CAPTCHA_IS_ERROR);
        RedisUtil.delete("seckillImageCode-userId:" + userid + "-goodsId:" + goodsId);
        return true;

    }

    @Override
    public void validateTime(Integer goodsId, Date nowDate) {
        Date goodsDate = (Date) RedisUtil.get("goodsId:" + goodsId + "-startDate");
        if (nowDate.before(goodsDate)) {
            throw new MyException(CodeMsg.GOODS_TIME_NOT_REACH);
        }
    }
}
