package com.wsz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class TbDifference implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer useId;

    private String username;

    private String equipment;

    private Integer lendNum;

    private Integer returnNum;

    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime differenceDate;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime returnTime;


    @TableField(exist = false)
    private Integer num;


    @TableField(exist = false)
    private Integer actualNum;


    @TableField(exist = false)
    private String changeReason;


}
