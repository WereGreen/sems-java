package com.wsz.entity;

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
public class TbEquipment extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String equipment;

    private String className;

    private Integer combination;

    private String equipments;


}
