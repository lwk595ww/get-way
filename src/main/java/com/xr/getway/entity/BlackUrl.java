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
 * @Date 2021/4/29 16:08
 * @Version 1.0
 * @Description 黑名单路径
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "black_url")
public class BlackUrl implements Serializable {
    private static final long serialVersionUID = 63413094784727232L;

    @Id
    @GeneratedValue(generator = "jdbc" ,strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopId;

    private String shopName;
}
