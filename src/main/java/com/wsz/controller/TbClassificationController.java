package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbClassification;
import com.wsz.entity.TbOperate;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @PostMapping("/revise")
    public Result revise(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);

        LambdaUpdateWrapper<TbClassification> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.eq(TbClassification::getClassName, tbClassification.getOldName());
        updateWrapper.set(TbClassification::getClassName, tbClassification.getClassName());

        tbClassificationService.update(null, updateWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbClassification.getOperationDate());
        operate.setOperationType(tbClassification.getOperationType());
        operate.setOperationClass(tbClassification.getOperationClass());
        operate.setUsername(tbClassification.getUsername());
        operate.setDetails(tbClassification.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "修改分类成功！", null);
    }

    @Transactional
    @PostMapping("/addClass")
    public Result addClass(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);

        TbClassification addClass = new TbClassification();
        addClass.setClassName(tbClassification.getClassName());

        tbClassificationService.save(addClass);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbClassification.getOperationDate());
        operate.setOperationType(tbClassification.getOperationType());
        operate.setOperationClass(tbClassification.getOperationClass());
        operate.setUsername(tbClassification.getUsername());
        operate.setDetails(tbClassification.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "添加分类成功！", null);
    }

    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbClassification tbClassification) {

        System.out.println(tbClassification);
        QueryWrapper<TbClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_name", tbClassification.getClassName());

        tbClassificationService.remove(queryWrapper);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbClassification.getOperationDate());
        operate.setOperationType(tbClassification.getOperationType());
        operate.setOperationClass(tbClassification.getOperationClass());
        operate.setUsername(tbClassification.getUsername());
        operate.setDetails(tbClassification.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "删除分类成功！", null);
    }


}
