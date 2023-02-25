package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbEquipment;
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
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/tb/equipment")
public class TbEquipmentController extends BaseController {

    @GetMapping("/info")
    public Result info (Principal principal) {

        List<TbEquipment> equipments = tbEquipmentService.list();

        return Result.suss(MapUtil.builder()
                .put("equipments", equipments)
                .map()
        );
    }

}
