package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yiqzq
 * @date 2020/6/7 13:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoods {
    private Double seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private Integer id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private Double goodsPrice;

    private Integer goodsStock;

    private Date createDate;

    private Date updateDate;

    private String goodsDetail;


}
/**
 (Double,Integer,Date,Date,String)
 ( 当天发/12分期/送大礼 Apple/苹果 iPhone X移动联通4G手机中移动)
 */