package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbDelay;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/tb/use")
public class TbUseController extends BaseController {

    @GetMapping("/info")
    public Result info(Principal principal) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", principal.getName());


        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );
    }

    @GetMapping("/returnInfo")
    public Result returnInfo(Principal principal) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", principal.getName());
        queryWrapper.eq("state", 0);


        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );
    }

    @PostMapping("/search")
    public Result search(@RequestBody TbUse tbUse) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbUse.getName());

        if (tbUse.getClassName() != "" && tbUse.getClassName() != null) {
            queryWrapper.eq("class_name", tbUse.getClassName());
        }

        if (tbUse.getEquipment() != "" && tbUse.getEquipment() != null) {
            queryWrapper.eq("equipment", tbUse.getEquipment());
        }

        if (tbUse.getWarehouse() != "" && tbUse.getWarehouse() != null) {
            queryWrapper.eq("warehouse", tbUse.getWarehouse());
        }

        if (tbUse.getReason() != "" && tbUse.getReason() != null) {
            queryWrapper.like("reason", tbUse.getReason());
        }

        if (tbUse.getApplyDate() != null) {
            queryWrapper.ge("apply_date", tbUse.getApplyDate());
        }

        if (tbUse.getReturnDate() != null) {
            queryWrapper.le("return_date", tbUse.getReturnDate());
        }

        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );

    }

    @PostMapping("/returnSearch")
    public Result returnSearch(@RequestBody TbUse tbUse) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbUse.getName());
        queryWrapper.eq("state", 0);

        if (tbUse.getClassName() != "" && tbUse.getClassName() != null) {
            queryWrapper.eq("class_name", tbUse.getClassName());
        }

        if (tbUse.getEquipment() != "" && tbUse.getEquipment() != null) {
            queryWrapper.eq("equipment", tbUse.getEquipment());
        }

        if (tbUse.getWarehouse() != "" && tbUse.getWarehouse() != null) {
            queryWrapper.eq("warehouse", tbUse.getWarehouse());
        }

        if (tbUse.getReason() != "" && tbUse.getReason() != null) {
            queryWrapper.like("reason", tbUse.getReason());
        }

        if (tbUse.getApplyDate() != null) {
            queryWrapper.ge("apply_date", tbUse.getApplyDate());
        }

        if (tbUse.getReturnDate() != null) {
            queryWrapper.le("return_date", tbUse.getReturnDate());
        }

        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );

    }

    @PostMapping("/addUse")
    public Result addUse(@RequestBody TbUse tbUse) {
        //用于存放前端发送过来的使用信息数据
        List<TbUse> tbUseList = new ArrayList<>();

        //用于存放用户使用的器材名称及器材分类数据
        List<Map<String, String>> equipments = tbUse.getEquipments();

        //查询数据条件对象
        LambdaUpdateWrapper<TbStock> updateWrapper = Wrappers.lambdaUpdate();

        //从数据库中取出库存表中的信息
        List<TbStock> stockList = tbStockService.list();

        //存放用户使用器材的数量列表
        List<Integer> updateStock = new ArrayList<>();

        //整理前端发送过来的数据，并将对应数据存放到对应的列表中
        for (int i = 0; i < equipments.size(); i++) {
            TbUse use = new TbUse();
            use.setName(tbUse.getName());

            use.setClassName(equipments.get(i).get("className"));
            use.setEquipment(equipments.get(i).get("value"));
            use.setNum(Integer.valueOf(equipments.get(i).get("num")));

            use.setWarehouse(tbUse.getWarehouse());
            use.setApplyDate(tbUse.getApplyDate());
            use.setReturnDate(tbUse.getReturnDate());
            use.setReason(tbUse.getReason());
            use.setState(tbUse.getState());

            for (int j = 0; j < stockList.size(); j++) {

                if (stockList.get(j).getWarehouse().equals(use.getWarehouse())) {
                    if (stockList.get(j).getEquipment().equals(use.getEquipment())) {
                        if (stockList.get(j).getStock() >= use.getNum()) {

                            updateStock.add(i, stockList.get(j).getStock() - use.getNum());

                            tbUseList.add(use);
                            if (tbUseList.size() == equipments.size()) {
                                break;
                            }
                        }
                    }
                }
            }

        }

        //如果存在库存不足的情况，向前端用户提示库存不足
        if (tbUseList.size() < equipments.size()) {
            throw new CaptchaException("库存不足！请确认仓库库存满足需求！");
        }

        //如库存充足，将对应使用信息保存进数据库使用表中，并更改对应库存信息
        for (int i = 0; i < tbUseList.size(); i++) {

            updateWrapper.eq(TbStock::getWarehouse, tbUseList.get(i).getWarehouse());
            updateWrapper.eq(TbStock::getEquipment, tbUseList.get(i).getEquipment());
            updateWrapper.set(TbStock::getStock, updateStock.get(i));

            tbStockService.update(null, updateWrapper);


        }

        tbUseService.saveBatch(tbUseList);

        return Result.suss(200,
                "添加使用成功",
                null);
    }

    @PostMapping("/delay")
    public Result delay(@RequestBody TbDelay tbDelay) {


        System.out.println(tbDelay);

        if (tbDelay.getQueryEndTime() != null) {
            tbDelay.setOriginalDate(tbDelay.getQueryEndTime());
            tbDelay.setDelayDate(tbDelay.getReturnTime());
        }

        if (tbDelay.getStartDate() != null) {
            tbDelay.setOriginalDate(tbDelay.getReturnTime());
            tbDelay.setDelayDate(tbDelay.getStartDate());
        }


        System.out.println(tbDelay);

        LambdaUpdateWrapper<TbUse> updateWrapper = Wrappers.lambdaUpdate();


        updateWrapper.eq(TbUse::getId, tbDelay.getUseId());

        if (tbDelay.getStartDate() != null) {
            updateWrapper.set(TbUse::getReturnDate, tbDelay.getStartDate());
        }

        if (tbDelay.getQueryEndTime() != null) {
            updateWrapper.set(TbUse::getReturnDate, tbDelay.getReturnTime());
        }


        tbUseService.update(null, updateWrapper);

        tbDelayService.save(tbDelay);


        return Result.suss(200,
                "延迟归还成功",
                null);
    }


    @PostMapping("/returnEquipment")
    public Result returnEquipment (@RequestBody TbUse tbUse) {

        System.out.println(tbUse);

        LambdaUpdateWrapper<TbUse> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbUse::getId, tbUse.getUseId());
        updateWrapper.set(TbUse::getReturnDate,tbUse.getReturnTime());
        updateWrapper.set(TbUse::getActualNum,tbUse.getActualNum());
        updateWrapper.set(TbUse::getState, 1);

        tbUseService.update(null, updateWrapper);

        LambdaUpdateWrapper<TbStock> updateStock = Wrappers.lambdaUpdate();
        QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("warehouse",tbUse.getWarehouse());
        queryWrapper.eq("equipment",tbUse.getEquipment());

        TbStock tbStock = tbStockService.getOne(queryWrapper);

        updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());
        updateStock.eq(TbStock::getEquipment, tbUse.getEquipment());
        updateStock.set(TbStock::getStock,tbStock.getStock() + tbUse.getActualNum());

        tbStockService.update(null,updateStock);

        return Result.suss(200,
                "归还器材成功",
                null);
    }

}
