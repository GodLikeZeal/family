package com.zeal.family.service;

import com.zeal.family.bo.SysUser;
import com.zeal.family.entiy.User;
import com.zeal.family.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service(value = "userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<User> users = userRepository.findByName(s);
        if (CollectionUtils.isEmpty(users)) {
            throw new UsernameNotFoundException("该用户不存在");
        } else {
            User user = users.get(0);
            return toSecuityUser(user);
        }
    }

    private SysUser toSecuityUser(User user) {
        Collection<GrantedAuthority> grantedAuthorities = Collections
            .singleton(new SimpleGrantedAuthority(user.getRole().name()));
        return new SysUser(user.getName(),user.getPassword(),grantedAuthorities);
    }
}
