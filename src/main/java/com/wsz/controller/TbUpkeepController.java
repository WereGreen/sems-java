package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUpkeep;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-04-11
 */
@RestController
@RequestMapping("/tb/upkeep")
public class TbUpkeepController extends BaseController {

    @GetMapping("/info")
    public Result info(TbUpkeep tbUpkeep) {

        QueryWrapper<TbUpkeep> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", tbUpkeep.getUsername());

        List<TbUpkeep> upkeepList = tbUpkeepService.list(queryWrapper);

        System.out.println(upkeepList);

        return Result.suss(MapUtil.builder()
                .put("upkeepList", upkeepList)
                .map()
        );
    }

    @GetMapping("/search")
    public Result search(TbUpkeep tbUpkeep) {

        QueryWrapper<TbUpkeep> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", tbUpkeep.getUsername());

        if (tbUpkeep.getWarehouse() != "" && tbUpkeep.getWarehouse() != null) {
            queryWrapper.eq("warehouse", tbUpkeep.getWarehouse());
        }

        if (tbUpkeep.getState() != null && !tbUpkeep.getState().equals("")) {
            queryWrapper.eq("state", tbUpkeep.getState());
        }

        List<TbUpkeep> upkeepList = tbUpkeepService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("upkeepList", upkeepList)
                .map()
        );
    }

    @Transactional
    @PostMapping("/accomplish")
    public Result accomplish(@RequestBody TbUpkeep tbUpkeep) {

        LambdaUpdateWrapper<TbUpkeep> updateUpkeep = Wrappers.lambdaUpdate();
        LambdaUpdateWrapper<TbStock> updateStock = Wrappers.lambdaUpdate();
        updateUpkeep.eq(TbUpkeep::getId, tbUpkeep.getId());
        updateUpkeep.set(TbUpkeep::getState, 1);
        updateUpkeep.set(TbUpkeep::getEndDate, tbUpkeep.getEndDate());

        tbUpkeepService.update(null, updateUpkeep);

        updateStock.eq(TbStock::getWarehouse,tbUpkeep.getWarehouse());
        updateStock.eq(TbStock::getEquipment, tbUpkeep.getEquipment());

        int stock = tbStockService.getOne(updateStock).getStock();

        updateStock.set(TbStock::getStock, (stock + tbUpkeep.getNum()));

        tbStockService.update(null, updateStock);

        return Result.suss(200, "完成维修成功！", null);
    }

}
