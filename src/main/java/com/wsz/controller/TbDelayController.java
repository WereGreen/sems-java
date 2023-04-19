package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbDelay;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/tb/delay")
public class TbDelayController extends BaseController {

    @GetMapping("/countInfo")
    public Result countInfo(Principal principal) {

        QueryWrapper<TbDelay> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", principal.getName());

        List<TbDelay> delayList = tbDelayService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("delayList", delayList)
                .map()
        );
    }

}
