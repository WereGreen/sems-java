package com.wsz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    private LocalDate operationDate;

    private String username;

    private String operationType;

    private String details;


}
