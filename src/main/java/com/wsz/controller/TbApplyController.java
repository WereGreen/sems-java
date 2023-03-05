package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
import com.wsz.entity.TbUse;
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
@RequestMapping("/tb/apply")
public class TbApplyController extends BaseController {

    @GetMapping("/info")
    public Result info () {

        List<TbApply> applyList = tbApplyService.list();

        return Result.suss(MapUtil.builder()
                .put("applyList", applyList)
                .map()
        );
    }

    @PostMapping("/search")
    public Result search (@RequestBody TbApply tbApply) {

        System.out.println(tbApply);

        QueryWrapper<TbApply> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", tbApply.getName());

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


    @PostMapping("/add")
    public Result add (@RequestBody TbApply tbApply) {

        System.out.println(tbApply);

        tbApplyService.save(tbApply);

        return Result.suss(200,"添加申请成功",null);
    }

}
