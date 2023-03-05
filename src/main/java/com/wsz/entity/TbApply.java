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
public class TbApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String equipment;

    private String className;

    private Integer num;

    private String warehouse;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime date;

    private String reason;

    private String state;

    private String classState;

    private String auditorName;

    private String auditorReason;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime auditorDate;


    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime queryStarTime;


    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime queryEndTime;


}
