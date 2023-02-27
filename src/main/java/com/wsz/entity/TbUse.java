package com.wsz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TbUse extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String name;

    private String className;

    private String equipment;

    private Integer num;

    private String warehouse;

    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime applyDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDateTime returnDate;

    private Integer state;

    @TableField(exist = false)
    private List<Map<String,String>> equipments = new ArrayList<>();;


}
