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
public class TbApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String equipment;

    private String className;

    private Integer num;

    private String warehouse;

    private LocalDateTime date;

    private String reason;

    private String state;

    private String auditorName;

    private String auditorReason;


}
