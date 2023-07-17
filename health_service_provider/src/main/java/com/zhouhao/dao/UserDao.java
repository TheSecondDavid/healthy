package com.zhouhao.dao;

import com.zhouhao.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    User loadUserByUsername(@Param("name") String name);
}
