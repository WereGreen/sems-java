package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.wsz.common.exception.CaptchaException;
import com.wsz.common.lang.Result;
import com.wsz.entity.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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
@RequestMapping("/tb/user")
public class TbUserController extends BaseController {


    @GetMapping("/nav")
    public Result nav(Principal principal) {
        TbUser tbUser = tbUserService.getByUsername(principal.getName());

        String authorityInfo = tbUserService.getUserAuthorityInfo(tbUser.getUsername());

        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        Menu menu = new Menu();
        menu = menu.normalMenu();
        String role = authorityInfoArray[0];

//        List<Menu> navs = Collections.singletonList(menu);
        List<Menu> navs =  new ArrayList<>();
        navs.add(menu);

        if (role.equals("ROLE_admin")) {
            Menu adminMenu = new Menu();
            adminMenu = adminMenu.adminMenu();
            navs.add(adminMenu);
        }

//        List<Menu> navs = Collections.singletonList(menu);

        return Result.suss(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map()
        );
    }


    @GetMapping("/admin")
    public Result admin(Principal principal) {

        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("role","admin");

        List<TbUser> adminList = tbUserService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("adminList", adminList)
                .map()
        );
    }

    @GetMapping("/allUser")
    public Result allUser() {

        List<TbUser> allUserList = tbUserService.list();

        return Result.suss(MapUtil.builder()
                .put("allUserList", allUserList)
                .map()
        );
    }

    @GetMapping("/search")
    public Result search(TbUser tbUser) {

        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();

        if (tbUser.getRole() != null && !tbUser.getRole().equals("")) {
            queryWrapper.eq("role", tbUser.getRole());
        }

        if (tbUser.getUsername() != null && !tbUser.getUsername().equals("")) {
            queryWrapper.like("username", tbUser.getUsername());
        }

        if (tbUser.getName() != null && !tbUser.getName().equals("")) {
            queryWrapper.like("name", tbUser.getName());
        }

        List<TbUser> userList = tbUserService.list(queryWrapper);

        return Result.suss(MapUtil.builder()
                .put("userList", userList)
                .map()
        );
    }

    @Transactional
    @PostMapping("/add")
    public Result add(@RequestBody TbUser tbUser) {

        TbUser addUser = new TbUser();
        addUser.setName(tbUser.getName());
        addUser.setUsername(tbUser.getUsername());
        addUser.setRole(tbUser.getRole());
        addUser.setPassword(bCryptPasswordEncoder.encode("888888"));

        System.out.println(bCryptPasswordEncoder.encode("888888"));

        tbUserService.save(addUser);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbUser.getOperationDate());
        operate.setOperationType(tbUser.getOperationType());
        operate.setOperationClass(tbUser.getOperationClass());
        operate.setUsername(tbUser.getOperationUsername());
        operate.setDetails(tbUser.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "新增用户成功！", null);
    }

    @Transactional
    @PostMapping("/update")
    public Result update(@RequestBody TbUser tbUser) {

        LambdaUpdateWrapper<TbUser> update = Wrappers.lambdaUpdate();

        update.eq(TbUser::getUsername, tbUser.getOldUsername());
        update.set(TbUser::getUsername, tbUser.getUsername());
        update.set(TbUser::getName, tbUser.getName());
        update.set(TbUser::getRole, tbUser.getRole());

        tbUserService.update(null, update);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbUser.getOperationDate());
        operate.setOperationType(tbUser.getOperationType());
        operate.setOperationClass(tbUser.getOperationClass());
        operate.setUsername(tbUser.getOperationUsername());
        operate.setDetails(tbUser.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "修改用户信息成功！", null);
    }

    @Transactional
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody TbUser tbUser) {

        LambdaUpdateWrapper<TbUser> update = Wrappers.lambdaUpdate();
        update.eq(TbUser::getUsername, tbUser.getUsername());

        TbUser user = tbUserService.getOne(update);

        if (!bCryptPasswordEncoder.matches(tbUser.getCurrentPass(), user.getPassword())) {
            throw new CaptchaException("密码错误！请重新输入！");
        }

        update.set(TbUser::getPassword, bCryptPasswordEncoder.encode(tbUser.getCheckPass()));

        tbUserService.update(null, update);

        return Result.suss(200, "修改用户密码成功！", null);
    }

    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody TbUser tbUser) {

        QueryWrapper<TbApply> applyWrapper = new QueryWrapper<>();
        applyWrapper.eq("name", tbUser.getUsername());
        applyWrapper.eq("state", 0);

        List<TbApply> applyList = tbApplyService.list(applyWrapper);
        if (applyList.size() > 0) {
            throw new CaptchaException("删除失败！该用户存在未处理的申请！");
        }

        QueryWrapper<TbUse> useWrapper = new QueryWrapper<>();
        useWrapper.eq("name", tbUser.getUsername());
        useWrapper.eq("state", 0);

        List<TbUse> useList = tbUseService.list(useWrapper);
        if (useList.size() > 0) {
            throw new CaptchaException("删除失败！该用户存在使用中的器材记录！");
        }

        QueryWrapper<TbUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", tbUser.getUsername());


        tbUserService.remove(userWrapper);

        return Result.suss(200, "删除用户信息成功！", null);
    }

    @Transactional
    @PostMapping("/resettingPassword")
    public Result resettingPassword(@RequestBody TbUser tbUser) {

        LambdaUpdateWrapper<TbUser> update = Wrappers.lambdaUpdate();
        update.eq(TbUser::getUsername, tbUser.getOldUsername());
        update.set(TbUser::getPassword, bCryptPasswordEncoder.encode("888888"));

        tbUserService.update(null, update);

        TbOperate operate = new TbOperate();

        operate.setOperationDate(tbUser.getOperationDate());
        operate.setOperationType(tbUser.getOperationType());
        operate.setOperationClass(tbUser.getOperationClass());
        operate.setUsername(tbUser.getOperationUsername());
        operate.setDetails(tbUser.getDetails());

        tbOperateService.save(operate);

        return Result.suss(200, "重置用户密码成功！", null);
    }

}
