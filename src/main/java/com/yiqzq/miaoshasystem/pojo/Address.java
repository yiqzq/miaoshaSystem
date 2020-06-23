package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiqzq
 * @date 2020/6/21 22:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Integer id;
    private Integer userId;
    private String telephone;
    private String address;
}
