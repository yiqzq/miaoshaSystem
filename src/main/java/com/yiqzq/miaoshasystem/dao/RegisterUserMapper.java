package com.yiqzq.miaoshasystem.dao;

import com.yiqzq.miaoshasystem.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author yiqzq
 * @date 2020/6/5 16:52
 */
@Mapper
@Component
public interface RegisterUserMapper {
    @Select("select * from user where phone =#{phone}")
    public User getUserByMobile(String phone);
    public int insertUser(User user);
}
