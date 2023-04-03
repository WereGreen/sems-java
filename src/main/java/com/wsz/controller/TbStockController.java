package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbOperate;
import com.wsz.entity.TbStock;
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
@RequestMapping("/tb/stock")
public class TbStockController extends BaseController {

    @GetMapping("/info")
    public Result info () {

        List<TbStock> stockList = tbStockService.list();

        return Result.suss(MapUtil.builder()
                .put("stockList", stockList)
                .map()
        );
    }


    @GetMapping("/search")
    public Result search (TbStock tbStock) {

        QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();

        if (tbStock.getWarehouse() != null && !tbStock.getWarehouse().equals("")) {
            queryWrapper.eq("warehouse",tbStock.getWarehouse());
        }

        if (tbStock.getEquipment() != null && !tbStock.getEquipment().equals("")) {
            queryWrapper.eq("equipment",tbStock.getEquipment());
        }

        List<TbStock> stockList = tbStockService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("stockList", stockList)
                .map()
        );
    }

    @Transactional
    @PostMapping("/update")
    public Result update (@RequestBody TbStock tbStock) {

        LambdaUpdateWrapper<TbStock> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbStock::getId, tbStock.getId());

        if (!tbStock.getWarehouse().equals(tbStock.getOldWarehouse())) {
            updateWrapper.set(TbStock::getWarehouse, tbStock.getWarehouse());
        }

        if (!tbStock.getTotalStock().equals(tbStock.getOldTotalStock())) {
            QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", tbStock.getId());

            TbStock stock = tbStockService.getOne(queryWrapper);
            int stockNum = stock.getStock();

            if ((tbStock.getOldTotalStock() - stockNum) > tbStock.getTotalStock()) {
                throw new CaptchaException("修改失败！修改后的库存小于该器材正在使用中的数量！");
            }

            int difference = tbStock.getTotalStock() - tbStock.getOldTotalStock();

            if (tbStock.getTotalStock() > tbStock.getOldTotalStock()) {

                updateWrapper.set(TbStock::getTotalStock, tbStock.getTotalStock());
                updateWrapper.set(TbStock::getStock, (stockNum + difference));
            } else {
                updateWrapper.set(TbStock::getTotalStock, tbStock.getTotalStock());
                updateWrapper.set(TbStock::getStock, (stockNum + difference));
            }
        }

        tbStockService.update(null, updateWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbStock.getOperationDate());
        operate.setOperationType(tbStock.getOperationType());
        operate.setOperationClass(tbStock.getOperationClass());
        operate.setUsername(tbStock.getUsername());
        operate.setDetails(tbStock.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "修改库存信息成功！", null);
    }

    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbStock tbStock) {

        System.out.println(tbStock);
        QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", tbStock.getId());

        TbStock stock = tbStockService.getOne(queryWrapper);
        if (stock.getTotalStock() != stock.getStock()) {
            throw new CaptchaException("删除失败！该库存存在使用中或维修中的情况！");
        }

        tbStockService.remove(queryWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbStock.getOperationDate());
        operate.setOperationType(tbStock.getOperationType());
        operate.setOperationClass(tbStock.getOperationClass());
        operate.setUsername(tbStock.getUsername());
        operate.setDetails(tbStock.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "删除库存成功！", null);
    }

    @Transactional
    @PostMapping("/add")
    public Result add(@RequestBody TbStock tbStock) {

        System.out.println(tbStock);

        QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("warehouse", tbStock.getWarehouse());
        queryWrapper.eq("equipment", tbStock.getEquipment());

        TbStock one = tbStockService.getOne(queryWrapper);

        if (one != null) {
            throw new CaptchaException("添加失败！该库存已存在！");
        }

        TbStock stock = new TbStock();
        stock.setWarehouse(tbStock.getWarehouse());
        stock.setEquipment(tbStock.getEquipment());
        stock.setStock(tbStock.getStock());
        stock.setTotalStock(tbStock.getStock());

        tbStockService.save(stock);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbStock.getOperationDate());
        operate.setOperationType(tbStock.getOperationType());
        operate.setOperationClass(tbStock.getOperationClass());
        operate.setUsername(tbStock.getUsername());
        operate.setDetails(tbStock.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "添加库存成功！", null);
    }

    @Transactional
    @PostMapping("/updateStockWarehouse")
    public Result updateStockWarehouse(@RequestBody TbStock tbStock) {

        LambdaUpdateWrapper<TbStock> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(TbStock::getWarehouse, tbStock.getOldWarehouse());
        updateWrapper.set(TbStock::getWarehouse, tbStock.getWarehouse());

        tbStockService.update(null, updateWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbStock.getOperationDate());
        operate.setOperationType(tbStock.getOperationType());
        operate.setOperationClass(tbStock.getOperationClass());
        operate.setUsername(tbStock.getUsername());
        operate.setDetails(tbStock.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "添加库存仓库成功！", null);
    }



}
