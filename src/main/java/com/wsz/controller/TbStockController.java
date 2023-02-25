package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbStock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/tb-stock")
public class TbStockController extends BaseController {

    @GetMapping("/info")
    public Result info () {

        List<TbStock> stockList = tbStockService.list();

        return Result.suss(MapUtil.builder()
                .put("stockList", stockList)
                .map()
        );
    }

}
