package com.wsz.entity;

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
public class TbDifference implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer useId;

    private String username;

    private Integer lendNum;

    private Integer returnNum;

    private String reason;


}
