package com.wsz.controller;

import com.wsz.common.lang.Result;
import com.wsz.entity.TbUser;
import com.wsz.service.impl.TbUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TbUserServiceImpl tbUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Result test() {
        return Result.suss(tbUserService.list());
    }

    @GetMapping("/test/pass")
    public Result pass() {

        // 加密后的密码
        String password = bCryptPasswordEncoder.encode("111111");
        boolean matches = bCryptPasswordEncoder.matches("111111", password);
        System.out.println("匹配结果: " + matches);
        return Result.suss(password);
    }


}
