package com.zhouhao.service;

import com.zhouhao.pojo.User;

public interface UserService {
    User loadUserByUsername(String name);
}
