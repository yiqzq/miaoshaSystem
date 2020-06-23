package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiqzq
 * @date 2020/6/8 11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsWIthStatusWithUser {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private SeckillGoods goods;
    private User user;
}
