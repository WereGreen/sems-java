package com.wsz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.entity.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

}
