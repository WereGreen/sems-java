package com.wsz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
public class TbOperate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "operation_id", type = IdType.AUTO)
    private Integer operationId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8")
    private LocalDate operationDate;

    private String username;

    private String operationType;

    private String details;


}
