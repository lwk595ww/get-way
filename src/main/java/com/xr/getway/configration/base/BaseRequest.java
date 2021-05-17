package com.xr.getway.configration.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *统一请求对象
 * @author xr
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseRequest<T>  implements Serializable {

    private static final long serialVersionUID = 761936423134142163L;


    /**
     * 请求内容
     */
    private T data;
}
