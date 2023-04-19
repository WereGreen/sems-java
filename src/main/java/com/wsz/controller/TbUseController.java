package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbDelay;
import com.wsz.entity.TbEquipment;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import org.springframework.transaction.annotation.Transactional;
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

//    @GetMapping("/userInfo")
//    private Result userInfo(TbUse tbUse) {
//
//        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name", tbUse.getName());
//
//        List<TbUse> useList = tbUseService.list(queryWrapper);
//
//        return Result.suss(MapUtil.builder()
//                .put("useList", useList)
//                .map()
//        );
//    }

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

    @Transactional
    @PostMapping("/addUse")
    public Result addUse(@RequestBody TbUse tbUse) {
        //用于存放前端发送过来的使用信息数据
        List<TbUse> tbUseList = new ArrayList<>();

        List<TbEquipment> equipmentList = tbEquipmentService.list();

        //用于存放用户使用的器材名称及器材分类数据
        List<Map<String, String>> equipments = tbUse.getEquipments();

        System.out.println("-----------------");
        System.out.println(equipments);
        System.out.println(tbUse.getEquipments().getClass());


        //查询数据条件对象
        LambdaUpdateWrapper<TbStock> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(TbStock::getWarehouse, tbUse.getWarehouse());

        //从数据库中取出库存表中的信息
        List<TbStock> stockList = tbStockService.list(updateWrapper);

        List<TbStock> addStock = new ArrayList<>();

        boolean state = true;

        //历遍前端发送过来的使用器材集合
        for (int i = 0; i < equipments.size(); i++) {
            //历遍数据库中所有的器材记录
            for (int j = 0; j < equipmentList.size(); j++) {
                //如果使用的器材与数据库中器材名称相等
                if (equipments.get(i).get("value").equals(equipmentList.get(j).getEquipment())) {
                    //判断该器材是否属于组合器材
                    if (equipmentList.get(j).getCombination() == 1) {
                        //如果是组合器材就历遍数据库库存表，查找该仓库是否有该器材
                        for (int k = 0; k < stockList.size(); k++) {
                            //如果有就直接将该器材添加到需要操作的库存列表中
                            if (equipments.get(i).get("value").equals(stockList.get(k).getEquipment())) {
                                TbStock tbStock = new TbStock();
                                tbStock.setEquipment(equipments.get(i).get("value"));
                                tbStock.setStock(Integer.valueOf(equipments.get(i).get("num")));
                                tbStock.setWarehouse(tbUse.getWarehouse());
                                addStock.add(tbStock);
                                state = false;
                                break;
                            }
                        }
                        System.out.println("++++++++++++++");
                        System.out.println(equipmentList.get(j).getEquipments());
                        //如果该仓库库存中没有该组合器材，则将该组合器材中的器材数据添加到需要操作的库存列表中
                        if (state) {
                            //将字符串转为jsonarray
                            JSONArray jsonArray = JSONUtil.parseArray(equipmentList.get(j).getEquipments());
                            //历遍该jsonarray，将组合器材数据添加到需要操作的库存列表中
                            for (int k = 0; k < jsonArray.size(); k++) {
                                TbStock tbStock = new TbStock();
                                tbStock.setEquipment(jsonArray.getJSONObject(k).getStr("equipment"));
                                tbStock.setStock(jsonArray.getJSONObject(k).getInt("num") * Integer.valueOf(equipments.get(i).get("num")));
                                tbStock.setWarehouse(tbUse.getWarehouse());
                                addStock.add(tbStock);
                            }
                        }
                        //如果不是组合器材则直接将器材数据添加到需要操作的库存列表中
                    } else {
                        TbStock tbStock = new TbStock();
                        tbStock.setEquipment(equipments.get(i).get("value"));
                        tbStock.setStock(Integer.valueOf(equipments.get(i).get("num")));
                        tbStock.setWarehouse(tbUse.getWarehouse());
                        addStock.add(tbStock);
                    }
                    state = true;
                    break;
                }
            }
            System.out.println("===============");
            System.out.println(addStock);
        }

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
            tbUseList.add(use);

        }

        System.out.println(stockList);

        int count = 0;

        for (int i = 0; i < addStock.size(); i++) {
            for (int j = 0; j < stockList.size(); j++) {
                if (addStock.get(i).getEquipment().equals(stockList.get(j).getEquipment())) {
                    count++;
                    if (addStock.get(i).getStock() > stockList.get(j).getStock()) {
                        throw new CaptchaException(addStock.get(i).getEquipment() + " 库存不足！请确认仓库库存是否满足需求！");
                    }
                    updateWrapper.eq(TbStock::getWarehouse, tbUse.getWarehouse());
                    System.out.println(tbUse.getWarehouse());
                    updateWrapper.eq(TbStock::getEquipment, addStock.get(i).getEquipment());
                    System.out.println(addStock.get(i).getEquipment());
                    List<TbStock> list = tbStockService.list(updateWrapper);
                    System.out.println(list);
                    updateWrapper.set(TbStock::getStock, (stockList.get(j).getStock() - addStock.get(i).getStock()));
                    System.out.println(stockList.get(j).getStock() - addStock.get(i).getStock());
                    tbStockService.update(null, updateWrapper);

                    updateWrapper = Wrappers.lambdaUpdate();
                    break;
                }
            }
        }

        if (addStock.size() != count) {
            throw new CaptchaException("该仓库中没有相关库存！请核对使用器材是否在仓库中存在库存！");
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

    @Transactional
    @PostMapping("/returnEquipment")
    public Result returnEquipment(@RequestBody TbUse tbUse) {

        System.out.println(tbUse);

        LambdaUpdateWrapper<TbUse> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbUse::getId, tbUse.getUseId());
        updateWrapper.set(TbUse::getReturnDate, tbUse.getReturnTime());
        updateWrapper.set(TbUse::getActualNum, tbUse.getActualNum());
        updateWrapper.set(TbUse::getState, 1);

        tbUseService.update(null, updateWrapper);

        List<TbEquipment> equipments = tbEquipmentService.list();

        LambdaUpdateWrapper<TbStock> updateStock = Wrappers.lambdaUpdate();
        updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());

        List<TbStock> stockList = tbStockService.list(updateStock);

        boolean state = true;

        //历遍数据库中器材数据
        for (int i = 0; i < equipments.size(); i++) {
            //如果使用的器材与数据库中器材名称相等就进行判断是否属于组合器材
            if (equipments.get(i).getEquipment().equals(tbUse.getEquipment())) {
                //如果属于组合器材
                if (equipments.get(i).getCombination() == 1) {
                    //历遍该仓库的库存信息，查找是否有该器材的组合库存
                    for (int j = 0; j < stockList.size(); j++) {
                        //如果有，则直接在该库存上做操作
                        if (tbUse.getEquipment().equals(stockList.get(j).getEquipment())) {
                            updateStock = Wrappers.lambdaUpdate();
                            updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());
                            updateStock.eq(TbStock::getEquipment, tbUse.getEquipment());
                            updateStock.set(TbStock::getStock, stockList.get(j).getStock() + tbUse.getActualNum());
                            tbStockService.update(null, updateStock);
                            state = false;
                            break;
                        }
                        //如果该仓库库存中没有该组合器材库存，则对组合器材的元素器材库存进行操作
                        if (state) {
                            //将字符串转为jsonarray
                            JSONArray jsonArray = JSONUtil.parseArray(equipments.get(i).getEquipments());
                            //历遍该jsonarray，将组合器材数据添加到需要操作的库存列表中
                            for (int k = 0; k < jsonArray.size(); k++) {
                                updateStock = Wrappers.lambdaUpdate();
                                updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());
                                updateStock.eq(TbStock::getEquipment, jsonArray.getJSONObject(k).getStr("equipment"));
                                Integer stock = tbStockService.getOne(updateStock).getStock();
                                updateStock.set(TbStock::getStock, stock + (jsonArray.getJSONObject(k).getInt("num") * tbUse.getActualNum()));
                                tbStockService.update(null, updateStock);
                            }
                        }
                        state = true;
                        break;
                    }

                } else {
                    for (int j = 0; j < stockList.size(); j++) {
                        if (equipments.get(i).getEquipment().equals(stockList.get(j).getEquipment())) {
                            updateStock = Wrappers.lambdaUpdate();
                            updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());
                            updateStock.eq(TbStock::getEquipment, tbUse.getEquipment());
                            updateStock.set(TbStock::getStock, stockList.get(j).getStock() + tbUse.getActualNum());
                            tbStockService.update(null, updateStock);
                            break;
                        }
                    }
                }
                break;
            }
        }

//        QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("warehouse", tbUse.getWarehouse());
//        queryWrapper.eq("equipment", tbUse.getEquipment());
//        tbStock = tbStockService.getOne(queryWrapper);
//
//        updateStock.eq(TbStock::getWarehouse, tbUse.getWarehouse());
//        updateStock.eq(TbStock::getEquipment, tbUse.getEquipment());
//        updateStock.set(TbStock::getStock, tbStock.getStock() + tbUse.getActualNum());
//
//        tbStockService.update(null, updateStock);

        return Result.suss(200,
                "归还器材成功",
                null);
    }

}
