package com.wsz.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbStock;
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
@RequestMapping("/tb/apply")
public class TbApplyController extends BaseController {

    @GetMapping("/info")
    public Result info(TbApply tbApply, Principal principal) {

        QueryWrapper<TbApply> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", principal.getName());
        queryWrapper.eq("class_state", tbApply.getClassState());

        List<TbApply> applyList = tbApplyService.list(queryWrapper);

        System.out.println(applyList);

        return Result.suss(MapUtil.builder()
                .put("applyList", applyList)
                .map()
        );
    }

    @PostMapping("/correlation")
    public Result correlation(@RequestBody TbApply tbApply, Principal principal) {

        System.out.println(principal.getName());
        System.out.println(tbApply);
        QueryWrapper<TbApply> queryWrapper = new QueryWrapper<>();
        if (!tbApply.getState().equals("") || tbApply.getState() != null) {
            queryWrapper.eq("state", tbApply.getState());
        }

        queryWrapper.eq("class_state", tbApply.getClassState());

        List<TbApply> applyList = tbApplyService.list(queryWrapper);

        queryWrapper = new QueryWrapper<TbApply>();
        queryWrapper.eq("class_state", tbApply.getClassState());
        queryWrapper.eq("auditor_name", principal.getName());
        applyList.addAll(tbApplyService.list(queryWrapper));

        return Result.suss(MapUtil.builder()
                .put("applyList", applyList)
                .map()
        );
    }

    @PostMapping("/search")
    public Result search(@RequestBody TbApply tbApply) {

        System.out.println(tbApply);

        QueryWrapper<TbApply> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbApply.getName());
        queryWrapper.eq("class_state", tbApply.getClassState());

        if (tbApply.getClassName() != "" && tbApply.getClassName() != null) {
            queryWrapper.eq("class_name", tbApply.getClassName());
        }

        if (tbApply.getEquipment() != "" && tbApply.getEquipment() != null) {
            queryWrapper.eq("equipment", tbApply.getEquipment());
        }

        if (tbApply.getWarehouse() != "" && tbApply.getWarehouse() != null) {
            queryWrapper.eq("warehouse", tbApply.getWarehouse());
        }

        if (tbApply.getReason() != "" && tbApply.getReason() != null) {
            queryWrapper.like("reason", tbApply.getReason());
        }

        if (tbApply.getState() != "" && tbApply.getState() != null) {
            queryWrapper.eq("state", tbApply.getState());
        }

        if (tbApply.getQueryStarTime() != null) {
            queryWrapper.ge("date", tbApply.getQueryStarTime());
        }

        if (tbApply.getQueryEndTime() != null) {
            queryWrapper.le("date", tbApply.getQueryEndTime());
        }

        if (tbApply.getAuditorName() != "" && tbApply.getAuditorName() != null) {
            queryWrapper.eq("auditor_name", tbApply.getAuditorName());
        }

        List<TbApply> applyList = tbApplyService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("applyList", applyList)
                .map()
        );
    }

    @PostMapping("/adminSearch")
    public Result adminSearch(@RequestBody TbApply tbApply) {

        System.out.println(tbApply);

        QueryWrapper<TbApply> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("class_state", tbApply.getClassState());

        if (tbApply.getState() != null && tbApply.getState() != "") {
            queryWrapper.eq("state", tbApply.getState());
            if (!tbApply.getState().equals("0")) {
                queryWrapper.eq("auditor_name", tbApply.getName());
            }

        }


        if (tbApply.getEquipment() != "" && tbApply.getEquipment() != null) {
            queryWrapper.eq("equipment", tbApply.getEquipment());
        }
        if (tbApply.getWarehouse() != "" && tbApply.getWarehouse() != null) {
            queryWrapper.eq("warehouse", tbApply.getWarehouse());
        }

        if (tbApply.getReason() != "" && tbApply.getReason() != null) {
            queryWrapper.like("reason", tbApply.getReason());
        }
        if (tbApply.getQueryStarTime() != null) {
            queryWrapper.ge("date", tbApply.getQueryStarTime());
        }

        if (tbApply.getQueryEndTime() != null) {
            queryWrapper.le("date", tbApply.getQueryEndTime());
        }

        List<TbApply> applyList = tbApplyService.list(queryWrapper);

        System.out.println(applyList);

        return Result.suss(MapUtil.builder()
                .put("applyList", applyList)
                .map()
        );
    }

    @PostMapping("/add")
    public Result add(@RequestBody TbApply tbApply) {

        System.out.println(tbApply);

        if (!tbApply.getClassState().equals("1")) {
            System.out.println("不是添加申请");
            QueryWrapper<TbStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("warehouse", tbApply.getWarehouse());
            queryWrapper.eq("equipment", tbApply.getEquipment());

            TbStock stock = tbStockService.getOne(queryWrapper);
            if (stock == null) {
                throw new CaptchaException("该仓库不存在此器材！");
            }
            if (stock.getTotalStock() < tbApply.getNum()) {
                throw new CaptchaException("库存不足！请确认仓库库存满足需求！");
            }

        }
        tbApplyService.save(tbApply);


        return Result.suss(200, "添加申请成功", null);
    }

    @Transactional
    @PostMapping("/handle")
    public Result handle(@RequestBody TbApply tbApply) {
        System.out.println("___________________");
        System.out.println(tbApply);

        LambdaUpdateWrapper<TbApply> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbApply::getId, tbApply.getId());
        updateWrapper.set(TbApply::getAuditorName, tbApply.getAuditorName());
        updateWrapper.set(TbApply::getAuditorReason, tbApply.getAuditorReason());
        updateWrapper.set(TbApply::getAuditorDate, tbApply.getAuditorDate());
        updateWrapper.set(TbApply::getState, tbApply.getState());

        tbApplyService.update(null, updateWrapper);

        if (tbApply.getState().equals("1")) {

            LambdaUpdateWrapper<TbStock> updateStock = Wrappers.lambdaUpdate();

            updateStock.eq(TbStock::getWarehouse, tbApply.getWarehouse());
            updateStock.eq(TbStock::getEquipment, tbApply.getEquipment());

            TbStock tbStock = tbStockService.getOne(updateStock);

            if (tbStock == null && tbApply.getClassState().equals("1")) {
                TbStock addStock = new TbStock();
                addStock.setWarehouse(tbApply.getWarehouse());
                addStock.setEquipment(tbApply.getEquipment());
                addStock.setStock(tbApply.getNum());
                addStock.setTotalStock(tbApply.getNum());

                tbStockService.save(addStock);

                return Result.suss(200, "处理申请成功！", null);
            }

            Integer stock = tbStock.getStock();

            if (tbApply.getClassState().equals("0")) {

                if (stock - tbApply.getNum() < 0) {
                    throw new CaptchaException("操作失败！对应库存不能满足该申请，请驳回！");
                }

                updateStock.set(TbStock::getStock, (stock - tbApply.getNum()));
                tbStockService.update(updateStock);

            } else if (tbApply.getClassState().equals("1")) {

                Integer totalStock = tbStock.getTotalStock();
                updateStock.set(TbStock::getStock, (stock + tbApply.getNum()));
                updateStock.set(TbStock::getTotalStock, (totalStock + tbApply.getNum()));
                tbStockService.update(updateStock);

            } else if (tbApply.getClassState().equals("-1")) {

                Integer totalStock = tbStock.getTotalStock();

                if (stock - tbApply.getNum() < 0 || totalStock - tbApply.getNum() < 0) {
                    throw new CaptchaException("操作失败！对应库存不能满足该申请，请驳回！");
                }

                updateStock.set(TbStock::getStock, (stock - tbApply.getNum()));
                updateStock.set(TbStock::getTotalStock, (totalStock - tbApply.getNum()));
                tbStockService.update(updateStock);

            } else {
                throw new CaptchaException("系统异常！");
            }
        }

        return Result.suss(200, "处理申请成功！", null);
    }

}
