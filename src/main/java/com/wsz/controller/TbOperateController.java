package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
import com.wsz.entity.TbOperate;
import org.springframework.expression.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wsz
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/tb/operate")
public class TbOperateController extends BaseController {

    @GetMapping("/info")
    public Result info(TbOperate tbOperate) {

        QueryWrapper<TbOperate> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("operation_class",tbOperate.getOperationClass());

        List<TbOperate> operateList = tbOperateService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("operateList", operateList)
                .map()
        );
    }

    @GetMapping("/search")
    public Result search(TbOperate tbOperate) {

        QueryWrapper<TbOperate> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("operation_class",tbOperate.getOperationClass());

        if (tbOperate.getUsername() != null && !tbOperate.getUsername().equals("")) {
            queryWrapper.eq("username", tbOperate.getUsername());
        }

        if (tbOperate.getOperationType() != null && !tbOperate.getOperationType().equals("")) {
            queryWrapper.eq("operation_type", tbOperate.getOperationType());
        }

        if (tbOperate.getDetails() != null && !tbOperate.getDetails().equals("")) {
            queryWrapper.eq("details", tbOperate.getDetails());
        }

        if (tbOperate.getQueryStarTime() != null) {
            queryWrapper.ge("operation_date", tbOperate.getOperationDate());
        }

        if (tbOperate.getQueryEndTime() != null) {
            queryWrapper.le("operation_date", tbOperate.getOperationDate());
        }


        List<TbOperate> operateList = tbOperateService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("operateList", operateList)
                .map()
        );
    }

}
