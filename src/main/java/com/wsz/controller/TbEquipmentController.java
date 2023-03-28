package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/update")
    public Result update (@RequestBody TbEquipment tbEquipment) {

        System.out.println(tbEquipment);

        LambdaUpdateWrapper<TbEquipment> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbEquipment::getClassName, tbEquipment.getOldName());
        updateWrapper.set(TbEquipment::getClassName, tbEquipment.getClassName());

        tbEquipmentService.update(null, updateWrapper);

        return Result.suss(200, "修改器材分类成功！", null);
    }

}
