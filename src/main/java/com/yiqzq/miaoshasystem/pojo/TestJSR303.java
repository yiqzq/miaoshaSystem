package com.yiqzq.miaoshasystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author yiqzq
 * @date 2020/6/6 13:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestJSR303 {
    @NotNull(message = "姓名能为空")
    private String name;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "电话不能为空")
    private String phone;
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

}
