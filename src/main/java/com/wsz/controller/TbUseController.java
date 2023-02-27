package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbStock;
import com.wsz.entity.TbUse;
import com.wsz.entity.TbUser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
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
        queryWrapper.eq("state",0);


        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );
    }

    @PostMapping("/search")
    public Result search(@RequestBody  TbUse tbUse) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbUse.getName());

        if (tbUse.getClassName() != "" && tbUse.getClassName() != null) {
            queryWrapper.eq("class_name",tbUse.getClassName());
        }

        if (tbUse.getEquipment() != "" && tbUse.getEquipment() != null) {
            queryWrapper.eq("equipment",tbUse.getEquipment());
        }

        if (tbUse.getWarehouse() != "" && tbUse.getWarehouse() != null) {
            queryWrapper.eq("warehouse",tbUse.getWarehouse());
        }

        if (tbUse.getReason() != "" && tbUse.getReason() != null) {
            queryWrapper.like("reason",tbUse.getReason());
        }

        if (tbUse.getApplyDate() != null) {
            queryWrapper.ge("apply_date",tbUse.getApplyDate());
        }

        if (tbUse.getReturnDate() != null) {
            queryWrapper.le("return_date",tbUse.getReturnDate());
        }

        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );

    }

    @PostMapping("/returnSearch")
    public Result returnSearch(@RequestBody  TbUse tbUse) {

        QueryWrapper<TbUse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbUse.getName());
        queryWrapper.eq("state",0);

        if (tbUse.getClassName() != "" && tbUse.getClassName() != null) {
            queryWrapper.eq("class_name",tbUse.getClassName());
        }

        if (tbUse.getEquipment() != "" && tbUse.getEquipment() != null) {
            queryWrapper.eq("equipment",tbUse.getEquipment());
        }

        if (tbUse.getWarehouse() != "" && tbUse.getWarehouse() != null) {
            queryWrapper.eq("warehouse",tbUse.getWarehouse());
        }

        if (tbUse.getReason() != "" && tbUse.getReason() != null) {
            queryWrapper.like("reason",tbUse.getReason());
        }

        if (tbUse.getApplyDate() != null) {
            queryWrapper.ge("apply_date",tbUse.getApplyDate());
        }

        if (tbUse.getReturnDate() != null) {
            queryWrapper.le("return_date",tbUse.getReturnDate());
        }

        List<TbUse> useList = tbUseService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("useList", useList)
                .map()
        );

    }

    @PostMapping("/addUse")
    public Result addUse(@RequestBody  TbUse tbUse) {

        System.out.println(tbUse);

        List<TbUse> tbUseList = new ArrayList<>();

        List<Map<String,String>> equipments = tbUse.getEquipments();

        LambdaUpdateWrapper<TbStock> updateWrapper = Wrappers.lambdaUpdate();

        List<TbStock> stockList = tbStockService.list();

        List<Integer> updateStock = new ArrayList<>();

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

                            updateStock.add(i,stockList.get(j).getStock() - use.getNum());

                            tbUseList.add(use);
                            if (tbUseList.size() == equipments.size()) {
                                break;
                            }
                        }
                    }
                }
            }

        }

        if (tbUseList.size() < equipments.size()) {
            throw new CaptchaException("库存不足！请确认仓库库存满足需求！");
        }

        for (int i = 0; i < tbUseList.size(); i++) {

            updateWrapper.eq(TbStock::getWarehouse,tbUseList.get(i).getWarehouse());
            updateWrapper.eq(TbStock::getEquipment,tbUseList.get(i).getEquipment());
            updateWrapper.set(TbStock::getStock,updateStock.get(i));

            tbStockService.update(null,updateWrapper);


        }

        tbUseService.saveBatch(tbUseList);

        return Result.suss(200,
                "添加使用成功",
                null);
    }

//    @PostMapping("/delay")
//    public Result delay(RequestBody )

}
