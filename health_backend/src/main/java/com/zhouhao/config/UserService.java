package com.zhouhao.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Component
public class UserService implements UserDetailsService {
    @Reference
    com.zhouhao.service.UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService.loadUserByUsername(name);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        return userDetail;
    }
}
