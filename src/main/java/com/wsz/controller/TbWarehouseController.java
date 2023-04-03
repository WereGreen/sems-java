package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbOperate;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import com.wsz.entity.TbWarehouse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search")
    public Result search(TbWarehouse tbWarehouse) {

        QueryWrapper<TbWarehouse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("warehouse", tbWarehouse.getWarehouse());

        List<TbWarehouse> warehouseList = tbWarehouseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("warehouseList", warehouseList)
                .map()
        );
    }

    @Transactional
    @PostMapping("/update")
    public Result update(@RequestBody TbWarehouse tbWarehouse) {

        LambdaUpdateWrapper<TbWarehouse> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(TbWarehouse::getWarehouse,tbWarehouse.getOldName());

        if (tbWarehouse.getWarehouse() != tbWarehouse.getOldName()) {
            updateWrapper.set(TbWarehouse::getWarehouse, tbWarehouse.getWarehouse());
        }
        if (tbWarehouse.getOldAddress() != tbWarehouse.getAddress()) {
            updateWrapper.set(TbWarehouse::getAddress, tbWarehouse.getAddress());
        }

        tbWarehouseService.update(null,updateWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbWarehouse.getOperationDate());
        operate.setOperationType(tbWarehouse.getOperationType());
        operate.setOperationClass(tbWarehouse.getOperationClass());
        operate.setUsername(tbWarehouse.getUsername());
        operate.setDetails(tbWarehouse.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "修改仓库信息成功！", null);
    }

    @Transactional
    @PostMapping("/add")
    public Result add(@RequestBody TbWarehouse tbWarehouse) {

        TbWarehouse warehouse = new TbWarehouse();
        warehouse.setWarehouse(tbWarehouse.getWarehouse());
        warehouse.setAddress(tbWarehouse.getAddress());

        tbWarehouseService.save(warehouse);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbWarehouse.getOperationDate());
        operate.setOperationType(tbWarehouse.getOperationType());
        operate.setOperationClass(tbWarehouse.getOperationClass());
        operate.setUsername(tbWarehouse.getUsername());
        operate.setDetails(tbWarehouse.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "添加仓库成功！", null);
    }

    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbWarehouse tbWarehouse) {

        System.out.println(tbWarehouse);
        QueryWrapper<TbWarehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("warehouse", tbWarehouse.getWarehouse());

        tbWarehouseService.remove(queryWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbWarehouse.getOperationDate());
        operate.setOperationType(tbWarehouse.getOperationType());
        operate.setOperationClass(tbWarehouse.getOperationClass());
        operate.setUsername(tbWarehouse.getUsername());
        operate.setDetails(tbWarehouse.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "删除仓库成功！", null);
    }

}
