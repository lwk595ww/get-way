package com.xr.getway.configration.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseEsPageRequestInfo implements Serializable {
    private static final long serialVersionUID = -4988838092450044667L;

    /**
     * 请求页
     */
    private int page;

    /**
     *每页行数
     */
    private int rows;


    /**
     * 排序字段，以及排序类型(code desc,name desc)，多个字段排序用,隔开
     */
    private String orderBy;

    private Integer isPaging;

}
