package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiqzq
 * @date 2020/6/17 17:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInSeckillqueue {
    private User user;
    private Integer goodsId;
}
