package com.health.security;

import com.health.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author W.Sun
 * @date 2019/11/9 18:59
 */
public class SpringSecurityUserService implements UserDetailsService{
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询数据库
        User user = findUserByUname(username);
        //判断是否为null
        if (user == null) {
            return null;
        }
        //将查询到的用户赋予权限
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
    }

    //模拟从数据库查询
    private User findUserByUname(String username){
        if("admin".equals(username)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode("123456"));
            return user;
        }
        return null;
    }
}
