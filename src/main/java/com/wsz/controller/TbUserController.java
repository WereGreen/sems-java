package com.wsz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.wsz.common.lang.Result;
import com.wsz.entity.Menu;
import com.wsz.entity.TbUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
