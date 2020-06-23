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
public class User {
    private int id;
    private String userName;
    private String phone;
    private String password;
    private String salt;
    private String head;
    private int loginCount;
    private Date registerDate;
    private Date lastLoginDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", head='" + head + '\'' +
                ", loginCount=" + loginCount +
                ", registerDate=" + registerDate +
                ", lastLoginDate=" + lastLoginDate +
                '}';
    }

    public static User mergeWithRegisterUser(RegisterUser registerUser, String salt) {
        Date nowTime = new Date();
        User user = new User();
        user.setPassword(registerUser.getPassword());
        user.setUserName(registerUser.getUsername());
        user.setPhone(registerUser.getMobile());
        user.setLoginCount(1);
        user.setRegisterDate(nowTime);
        user.setLastLoginDate(nowTime);
        user.setSalt(salt);
        return user;
    }
}
