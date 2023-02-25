package com.wsz.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Menu implements Serializable{

    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<Menu> children = new ArrayList<>();


    public Menu normalMenu() {

        Menu normalMenu = new Menu();

        Menu equipmentApply = new Menu();

        equipmentApply.setName("EquipmentApply");
        equipmentApply.setTitle("体育器材使用");
        equipmentApply.setIcon("el-icon-edit-outline");
        equipmentApply.setComponent("sysFun/EquipmentApply");
        equipmentApply.setPath("/sysFun/EquipmentApply");

        Menu equipmentReturn = new Menu();

        equipmentReturn.setName("EquipmentReturn");
        equipmentReturn.setTitle("体育器材归还");
        equipmentReturn.setIcon("el-icon-s-claim");
        equipmentReturn.setComponent("sysFun/EquipmentReturn");
        equipmentReturn.setPath("/sysFun/EquipmentReturn");

        Menu addEquipment = new Menu();

        addEquipment.setName("AddEquipment");
        addEquipment.setTitle("器材添加申请");
        addEquipment.setIcon("el-icon-document-add");
        addEquipment.setComponent("sysFun/AddEquipment");
        addEquipment.setPath("/sysFun/AddEquipment");

        Menu scrapEquipment = new Menu();

        scrapEquipment.setName("ScrapEquipment");
        scrapEquipment.setTitle("器材报废申请");
        scrapEquipment.setIcon("el-icon-document-remove");
        scrapEquipment.setComponent("sysFun/ScrapEquipment");
        scrapEquipment.setPath("/sysFun/ScrapEquipment");

        Menu repairEquipment = new Menu();

        repairEquipment.setName("RepairEquipment");
        repairEquipment.setTitle("器材维修申请");
        repairEquipment.setIcon("el-icon-s-cooperation");
        repairEquipment.setComponent("sysFun/RepairEquipment");
        repairEquipment.setPath("/sysFun/RepairEquipment");


        normalMenu.setName("SysFun");
        normalMenu.setTitle("系统功能");
        normalMenu.setIcon("el-icon-s-operation");
        normalMenu.setComponent("");
        normalMenu.setPath("");
        normalMenu.children.add(equipmentApply);
        normalMenu.children.add(equipmentReturn);
        normalMenu.children.add(addEquipment);
        normalMenu.children.add(scrapEquipment);
        normalMenu.children.add(repairEquipment);

        return normalMenu;
    }

    public Menu adminMenu() {

        Menu adminMenu = new Menu();

        Menu equipmentManagement = new Menu();

        equipmentManagement.setName("EquipmentManagement");
        equipmentManagement.setTitle("器材申请管理");
        equipmentManagement.setIcon("el-icon-s-check");
        equipmentManagement.setComponent("sysMg/EquipmentManagement");
        equipmentManagement.setPath("/sysMg/EquipmentManagement");

        Menu equipmentClassification = new Menu();

        equipmentClassification.setName("EquipmentClassification");
        equipmentClassification.setTitle("器材管理 / 分类");
        equipmentClassification.setIcon("el-icon-s-grid");
        equipmentClassification.setComponent("sysMg/EquipmentClassification");
        equipmentClassification.setPath("/sysMg/EquipmentClassification");

        Menu equipmentWarehouse = new Menu();

        equipmentWarehouse.setName("EquipmentWarehouse");
        equipmentWarehouse.setTitle("器材仓库管理");
        equipmentWarehouse.setIcon("el-icon-school");
        equipmentWarehouse.setComponent("sysMg/EquipmentWarehouse");
        equipmentWarehouse.setPath("/sysMg/EquipmentWarehouse");

        Menu userManagement = new Menu();

        userManagement.setName("UserManagement");
        userManagement.setTitle("用户管理");
        userManagement.setIcon("el-icon-user-solid");
        userManagement.setComponent("sysUser/UserManagement");
        userManagement.setPath("/sysUser/UserManagement");


        adminMenu.setName("SysMg");
        adminMenu.setTitle("系统管理");
        adminMenu.setIcon("el-icon-s-tools");
        adminMenu.setComponent("");
        adminMenu.setPath("");
        adminMenu.children.add(equipmentManagement);
        adminMenu.children.add(equipmentClassification);
        adminMenu.children.add(equipmentWarehouse);
        adminMenu.children.add(userManagement);

        return adminMenu;
    }


}
