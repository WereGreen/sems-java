package com.wsz.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author wsz
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbDelay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 使用表中的编号
     */
    private Integer useId;

    /**
     * 使用人的用户名
     */
    private String username;

    /**
     * 延迟归还原因
     */
    private String delayReason;

    /**
     * 原本预计归还时间
     */
    private LocalDateTime originalDate;

    /**
     * 延迟归还时间
     */
    private LocalDateTime delayDate;


    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime returnTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime startDate;


    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime queryEndTime;


}
