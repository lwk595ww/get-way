package com.xr.getway.configration.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author xr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasePageResponseInfo <T> implements Serializable {

    private static final long serialVersionUID = -6478947219913646207L;
    /**
     * 当前页
     */
    private int page;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总记录条数
     */
    private long records;

    /**
     * 返回内容
     */
    private List<T> rows;

}
