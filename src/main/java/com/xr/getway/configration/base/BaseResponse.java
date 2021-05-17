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
public class BaseResponse<T>  implements Serializable {
    private static final long serialVersionUID = -6506338369905636078L;


    /**
     * 返回代码描述
     */
    private String message;

    /**
     * 返回状态
     */
    private Integer status;


    /**
     * 返回内容
     */
    private T data;



}
