package com.wsz.security;

import com.wsz.entity.TbUser;
import com.wsz.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    TbUserService tbUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbUser tbUser = tbUserService.getByUsername(username);

        if (tbUser == null) {
            throw new UsernameNotFoundException("用户名或密码不正确！");
        }

        return new AccountUser(tbUser.getUsername(),tbUser.getPassword(),tbUser.getRole());
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param username
     * @return
     */

    public List<GrantedAuthority> getUserAuthority(String username) {

        String authority = tbUserService.getUserAuthorityInfo(username);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }


}
