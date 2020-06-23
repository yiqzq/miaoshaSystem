package com.yiqzq.miaoshasystem.pojo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private Integer id;

    private Integer userId;

    private Integer goodsId;

    private Integer addrId;

    private String goodsName;

    private String goodsImg;

    private Integer goodsCount;

    private Double goodsPrice;

    private Integer orderChannel;

    private Integer status;

    private Date createDate;

    private Date payDate;

}