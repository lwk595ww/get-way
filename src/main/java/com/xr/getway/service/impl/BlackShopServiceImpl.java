package com.xr.getway.service.impl;

import com.xr.getway.entity.BlackShop;
import com.xr.getway.mapper.BlackShopMapper;
import com.xr.getway.service.BlackShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author lwk
 * @Date 2021/4/29 17:01
 * @Version 1.0
 * @Description
 */
@Service
public class BlackShopServiceImpl implements BlackShopService {

    @Autowired
    private BlackShopMapper blackShopMapper;

    @Override
    public List<BlackShop> selectList() {
        return blackShopMapper.selectAll();
    }
}
