package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import com.wsz.service.TbUseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
@RequestMapping("/tb/equipment")
public class TbEquipmentController extends BaseController {

    @GetMapping("/info")
    public Result info(Principal principal) {

        List<TbEquipment> equipments = tbEquipmentService.list();

        return Result.suss(MapUtil.builder()
                .put("equipments", equipments)
                .map()
        );
    }

    @PostMapping("/update")
    public Result update(@RequestBody TbEquipment tbEquipment) {

        System.out.println(tbEquipment);

        LambdaUpdateWrapper<TbEquipment> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbEquipment::getClassName, tbEquipment.getOldName());
        updateWrapper.set(TbEquipment::getClassName, tbEquipment.getClassName());

        tbEquipmentService.update(null, updateWrapper);

        return Result.suss(200, "修改器材分类成功！", null);
    }

    @PostMapping("/updateEquipment")
    public Result updateEquipment(@RequestBody TbEquipment tbEquipment) {

        System.out.println(tbEquipment);

        LambdaUpdateWrapper<TbEquipment> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbEquipment::getEquipment, tbEquipment.getOldName());

        if (tbEquipment.getClassName() != null && !tbEquipment.getEquipment().equals("")
            && !tbEquipment.getClassName().equals(tbEquipment.getOldClassName())) {
            updateWrapper.set(TbEquipment::getClassName, tbEquipment.getClassName());
        }

        if (tbEquipment.getCombination() != null && !tbEquipment.getEquipment().equals("")) {

            updateWrapper.set(TbEquipment::getCombination, tbEquipment.getCombination());

            if (tbEquipment.getCombination().equals(0)) {
                updateWrapper.set(TbEquipment::getEquipments, "");
            }

            if (tbEquipment.getCombination().equals(1) &&
                    tbEquipment.getNewEquipments() != null &&
                    !tbEquipment.getNewEquipments().equals("")) {

                updateWrapper.set(TbEquipment::getEquipments, tbEquipment.getNewEquipments());
            }
        }

        if (tbEquipment.getEquipment() != null && !tbEquipment.getEquipment().equals("")
                && !tbEquipment.getEquipment().equals(tbEquipment.getOldName())) {

            updateWrapper.set(TbEquipment::getEquipment, tbEquipment.getEquipment());
        }


        tbEquipmentService.update(null, updateWrapper);

        return Result.suss(200, "修改器材成功！", null);
    }

    @GetMapping("/search")
    public Result search(TbEquipment tbEquipment) {

        QueryWrapper<TbEquipment> queryWrapper = new QueryWrapper<>();

        if (tbEquipment.getEquipment() != null && tbEquipment.getEquipment() != "") {
            queryWrapper.eq("equipment", tbEquipment.getEquipment());
        }

        if (tbEquipment.getClassName() != null && tbEquipment.getClassName() != "") {
            queryWrapper.eq("class_name", tbEquipment.getClassName());
        }

        List<TbEquipment> equipments = tbEquipmentService.list(queryWrapper);

        System.out.println(equipments);

        return Result.suss(MapUtil.builder()
                .put("equipments", equipments)
                .map()
        );
    }

    @PostMapping("/addEquipment")
    public Result addEquipment(@RequestBody TbEquipment tbEquipment) {

        System.out.println(tbEquipment);

        TbEquipment equipment = new TbEquipment();
        equipment.setEquipment(tbEquipment.getEquipment());
        equipment.setClassName(tbEquipment.getClassName());
        equipment.setCombination(tbEquipment.getCombination());

        if (tbEquipment.getCombination().equals(1)) {
            equipment.setEquipments(tbEquipment.getNewEquipments());
        }

        tbEquipmentService.save(equipment);


        return Result.suss(200, "添加器材成功！", null);
    }

    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbEquipment tbEquipment) {

        System.out.println(tbEquipment);

        QueryWrapper<TbUse> queryUse = new QueryWrapper<>();
        QueryWrapper<TbStock> queryStock = new QueryWrapper<>();

        queryUse.eq("equipment", tbEquipment.getEquipment());
        queryUse.eq("state", 1);

        List<TbUse> useList = tbUseService.list(queryUse);
        if (useList.size() > 0 ) {
            throw new CaptchaException("删除失败！该器材仍在使用中！请归还后再次操作");
        }

        queryStock.eq("equipment", tbEquipment.getEquipment());

        List<TbStock> stockList = tbStockService.list(queryStock);
        if (stockList.size() > 0) {
            throw new CaptchaException("删除失败！该器材仍有库存！请删除库存后后再次操作");
        }

        QueryWrapper<TbEquipment> queryEquipment = new QueryWrapper<>();

        queryEquipment.eq("equipment", tbEquipment.getEquipment());

        tbEquipmentService.remove(queryEquipment);

        return Result.suss(200, "删除分类成功！", null);
    }

}
