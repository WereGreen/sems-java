package com.wsz.controller;

import com.wsz.service.*;
import com.wsz.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    TbUserService tbUserService;

    @Autowired
    TbUseService tbUseService;

    @Autowired
    TbEquipmentService tbEquipmentService;

    @Autowired
    TbApplyService tbApplyService;

    @Autowired
    TbClassificationService tbClassificationService;

    @Autowired
    TbStockService tbStockService;

    @Autowired
    TbWarehouseService tbWarehouseService;


    @Autowired
    TbOperateService tbOperateService;


    @Autowired
    TbDifferenceService tbDifferenceService;

    @Autowired
    TbDelayService tbDelayService;

}
