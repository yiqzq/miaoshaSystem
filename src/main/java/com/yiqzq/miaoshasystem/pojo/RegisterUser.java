package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yiqzq
 * @date 2020/6/5 12:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String username;
    private String mobile;
    private String password;
    private String checkpassword;
    private String code;
    private int passwordLength;
}
