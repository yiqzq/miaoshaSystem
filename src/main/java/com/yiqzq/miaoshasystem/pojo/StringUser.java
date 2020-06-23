package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yiqzq
 * @date 2020/6/4 13:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringUser {
    private int id;
    private String userName;
    private String phone;
    private String password;
    private String salt;
    private String head;
    private int loginCount;
    private String registerDate;
    private String lastLoginDate;

    public StringUser(User user) {
        id = user.getId();
        userName = user.getUserName();
        phone = user.getPhone();
        salt = user.getSalt();
        head = user.getHead();
        loginCount = user.getLoginCount();
    }

}
