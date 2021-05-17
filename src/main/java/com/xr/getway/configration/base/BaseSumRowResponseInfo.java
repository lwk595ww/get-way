package com.xr.getway.configration.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author : lwk
 * @Description
 * @Date: Created in 15:23 2021/1/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseSumRowResponseInfo<T,V> implements Serializable {
    private static final long serialVersionUID = -5223111766454746903L;

    /**
     * 第一个结果集条数
     */
    private Integer records;

    /**
     * 第二个结果集条数
     */
    private Integer records2;

    /**
     * 第一个结果集
     */
    private T rows;

    /**
     * 第二个结果集
     */
    private V sumRow;
}
