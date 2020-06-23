package com.yiqzq.miaoshasystem.pojo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
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