package com.wsz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wsz
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TbEquipment extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String equipment;

    private String className;

    private Integer combination;

    private String equipments;

    @TableField(exist = false)
    private String oldName;

    @TableField(exist = false)
    private String oldClassName;

    @TableField(exist = false)
    private Integer oldCombination;

    @TableField(exist = false)
    private String newEquipments;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @TableField(exist = false)
    private LocalDateTime operationDate;

    @TableField(exist = false)
    private Integer operationType;

    @TableField(exist = false)
    private String details;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private Integer operationClass;

}
