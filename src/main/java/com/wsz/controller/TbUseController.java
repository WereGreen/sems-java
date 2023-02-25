package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/tb-use")
public class TbUseController extends BaseController {

    @GetMapping("/info")
    public Result info() {

        List<TbUse> useList = tbUseService.list();

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );
    }
}
