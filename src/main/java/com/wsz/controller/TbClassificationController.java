package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/tb/classification")
public class TbClassificationController extends BaseController {

    @GetMapping("/info")
    public Result info() {

        List<TbClassification> classifications = tbClassificationService.list();

        return Result.suss(MapUtil.builder()
                .put("classifications", classifications)
                .map()
        );
    }

    @GetMapping("/search")
    public Result search(TbClassification tbClassification) {

        System.out.println(tbClassification);

        QueryWrapper<TbClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("class_name", tbClassification.getClassName());

        List<TbClassification> classifications = tbClassificationService.list(queryWrapper);

        System.out.println(classifications);

        return Result.suss(MapUtil.builder()
                .put("classifications", classifications)
                .map()
        );
    }

    @PostMapping("/revise")
    public Result revise(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);

        LambdaUpdateWrapper<TbClassification> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbClassification::getClassName, tbClassification.getOldName());
        updateWrapper.set(TbClassification::getClassName, tbClassification.getClassName());

        tbClassificationService.update(null, updateWrapper);

        return Result.suss(200, "修改分类成功！", null);
    }

    @PostMapping("/addClass")
    public Result addClass(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);

        TbClassification addClass = new TbClassification();
        addClass.setClassName(tbClassification.getClassName());

        tbClassificationService.save(addClass);

        return Result.suss(200, "添加分类成功！", null);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);
        QueryWrapper<TbClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_name", tbClassification.getClassName());

        tbClassificationService.remove(queryWrapper);


        return Result.suss(200, "删除分类成功！", null);
    }


}
