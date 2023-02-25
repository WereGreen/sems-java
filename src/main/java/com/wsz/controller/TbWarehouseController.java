package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbUse;
import com.wsz.entity.TbWarehouse;
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
@RequestMapping("/tb/warehouse")
public class TbWarehouseController extends BaseController {

    @GetMapping("/info")
    public Result info() {

        List<TbWarehouse> warehouseList = tbWarehouseService.list();

        return Result.suss(MapUtil.builder()
                .put("warehouseList", warehouseList)
                .map()
        );
    }

}
