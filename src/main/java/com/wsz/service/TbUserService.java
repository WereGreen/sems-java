package com.wsz.service;

import com.wsz.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsz.mapper.TbUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
public interface TbUserService extends IService<TbUser> {

    TbUser getByUsername(String username);


    String getUserAuthorityInfo(String username);
}
