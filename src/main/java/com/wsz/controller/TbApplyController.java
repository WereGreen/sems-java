package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbApply;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
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


}
