package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbDifference;
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
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/tb/difference")
public class TbDifferenceController extends BaseController {

    @GetMapping("/info")
    public Result info(Principal principal) {

        QueryWrapper<TbDifference> queryWrapper = new QueryWrapper<>();

        System.out.println(principal.getName());

        queryWrapper.eq("username", principal.getName());

        List<TbDifference> differenceList = tbDifferenceService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("differenceList", differenceList)
                .map()
        );
    }

    @PostMapping("/add")
    public Result add (@RequestBody TbDifference tbDifference) {

        System.out.println(tbDifference);

        tbDifference.setLendNum(tbDifference.getNum());
        tbDifference.setReturnNum(tbDifference.getActualNum());
        tbDifference.setReason(tbDifference.getChangeReason());
        tbDifference.setDifferenceDate(tbDifference.getReturnTime());

        tbDifferenceService.save(tbDifference);


        return Result.suss(200,
                "记录差异成功！~",
                null);
    }

}
