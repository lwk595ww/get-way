package com.xr.getway.controller;

import com.xr.getway.entity.BlackShop;
import com.xr.getway.entity.BlackUrl;
import com.xr.getway.mapper.BlackShopMapper;
import com.xr.getway.mapper.BlackUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lwk
 * @Date 2021/4/29 15:40
 * @Version 1.0
 * @Description
 */
@RestController
@RequestMapping("/getWay")
public class BlackShopController {

    @Autowired
    private BlackShopMapper shopMapper;

    @Autowired
    private BlackUrlMapper blackUrlMapper;

    @RequestMapping(value = "/blackShopAdd",method = RequestMethod.POST)
    public Integer blackShopAdd(@RequestBody BlackShop blackShop){
        return shopMapper.insert(blackShop);
    }

    @RequestMapping(value = "/blackShopDel/{id}",method = RequestMethod.GET)
    public Integer blackShopDel(@PathVariable("id") Long id){
        return shopMapper.deleteByPrimaryKey(id);
    }

    @RequestMapping(value = "/blackUrlAdd",method = RequestMethod.POST)
    public Integer blackUrlAdd(@RequestBody BlackUrl blackUrl){
        return blackUrlMapper.insert(blackUrl);
    }

    @RequestMapping(value = "/blackUrlDel/{id}",method = RequestMethod.GET)
    public Integer blackUrlDel(@PathVariable("id") Long id){
        return blackUrlMapper.deleteByPrimaryKey(id);
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "路由中的测试方法！！";
    }
}
