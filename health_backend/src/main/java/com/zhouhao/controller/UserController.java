package com.zhouhao.controller;

import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("all")
@RestController
@RequestMapping("user")
public class UserController {
    @RequestMapping("getName")
    public Result getName(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
    }
}
