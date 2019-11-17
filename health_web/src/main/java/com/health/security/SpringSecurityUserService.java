package com.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.PermissionService;
import com.health.service.RoleService;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author W.Sun
 * @date 2019/11/10 15:34
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        //判断User是否存在，不存在，则返回null
        if (user == null) {
            return null;
        }
        //根据用户id查询角色
        Set<Role> roleSet = roleService.findRoleByUserId(user.getId());
        List<GrantedAuthority> list = new ArrayList<>();
        //遍历角色列表，将角色和橘色对应的权限授给用户
        for (Role role : roleSet) {
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissionSet = permissionService.findPermissionByRoleId(role.getId());
            for (Permission permission : permissionSet) {
                String keyword = permission.getKeyword();
                list.add(new SimpleGrantedAuthority(keyword));
            }
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
    }
}
