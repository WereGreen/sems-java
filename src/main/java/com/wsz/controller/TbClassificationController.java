package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbEquipment;
import com.wsz.service.TbClassificationService;
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
@RequestMapping("/tb/classification")
public class TbClassificationController extends BaseController {

    @GetMapping("/info")
    public Result info () {

        List<TbClassification> classifications = tbClassificationService.list();

        return Result.suss(MapUtil.builder()
                .put("classifications", classifications)
                .map()
        );
    }

}
