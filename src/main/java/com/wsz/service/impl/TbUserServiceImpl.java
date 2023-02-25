package com.wsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsz.entity.TbUser;
import com.wsz.mapper.TbUserMapper;
import com.wsz.service.TbUserService;
import com.wsz.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */

@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    @Autowired
    TbUserService tbUserService;

    @Autowired
    TbUserMapper tbUserMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public TbUser getByUsername(String username) {
        return getOne(new QueryWrapper<TbUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(String username) {

        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + username)) {
            authority = (String) redisUtil.get("GrantedAuthority:" + username);
        } else {

            List<TbUser> roles = tbUserService.list(new QueryWrapper<TbUser>().select("role").eq("username", username));

            if (roles.size() > 0) {
                String role = roles.stream().map(r -> "ROLE_" + r.getRole()).collect(Collectors.joining(","));
                authority = role;
            }

            redisUtil.set("GrantedAuthority:" + username, authority, 60 * 60 * 24);
        }


        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }


}
