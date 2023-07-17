package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhouhao.dao.UserDao;
import com.zhouhao.pojo.User;
import com.zhouhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User loadUserByUsername(String name) {
        return userDao.loadUserByUsername(name);
    }
}
