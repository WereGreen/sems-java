package com.wsz.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    private LocalDateTime applyDate;

    private LocalDateTime returnDate;

    private Integer state;


}
