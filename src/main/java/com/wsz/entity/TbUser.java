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
public class TbUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String name;

    private String role;

    @TableField(exist = false)
    private String oldUsername;

    @TableField(exist = false)
    private String oldName;

    @TableField(exist = false)
    private String oldRole;

    @TableField(exist = false)
    private String checkPass;

    @TableField(exist = false)
    private String currentPass;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    @TableField(exist = false)
    private LocalDateTime operationDate;

    @TableField(exist = false)
    private Integer operationType;

    @TableField(exist = false)
    private String operationUsername;

    @TableField(exist = false)
    private String details;

    @TableField(exist = false)
    private Integer operationClass;


}
