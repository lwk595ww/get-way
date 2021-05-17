package com.xr.getway.configration.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页属性
 * @author xr
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasePageRequestInfo<T> implements Serializable {

    private static final long serialVersionUID = -1215305737793693817L;
    /**
     * 请求页
     */
    @NotNull(message = "请求页不能为null")
    @Min(value = 1,message = "请求页必须大于等于1")
    private Integer page;

    /**
     *每页行数
     */
    @NotNull(message = "每页行数不能为null")
    @Min(value = 1,message = "每页行数必须大于等于1")
    private Integer rows;

    /**
     * 关键字
     */
    private String searchText;

    /**
     * 排序字段，以及排序类型(code desc,name desc)
     * ，多个字段排序用,隔开
     *
     */
    @NotBlank(message = "排序字段不能为空")
    @NotNull(message = "排序字段不能为null")
    private String orderBy;


    private Integer isPaging;


    private Integer isReturnSumRow;

    /**
     * 其他条件
     */
    private T condition;
}
