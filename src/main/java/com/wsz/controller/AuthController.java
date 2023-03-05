package com.wsz.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.wsz.common.lang.Const;
import com.wsz.common.lang.Result;
import com.wsz.entity.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
public class AuthController extends BaseController {

    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        //测试代码
        key = "aaaaa";
        code = "11111";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        return Result.suss(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build()
        );

    }

    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/tb/userInfo")
    public Result userInfo(Principal principal) {

        TbUser tbUser = tbUserService.getByUsername(principal.getName());
        return Result.suss(MapUtil.builder()
                .put("username", tbUser.getUsername())
                .put("name", tbUser.getName())
                .put("userRole", tbUser.getRole())
                .map()
        );
    }

}
