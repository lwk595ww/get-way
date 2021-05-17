package com.xr.getway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author lwk
 * @Date 2021/4/29 16:05
 * @Version 1.0
 * @Description 店铺黑名单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "black_shop")
public class BlackShop implements Serializable {

    private static final long serialVersionUID = -7043292600560981106L;

    @Id
    @GeneratedValue(generator = "jdbc" ,strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopId;

    private String shopName;
}
