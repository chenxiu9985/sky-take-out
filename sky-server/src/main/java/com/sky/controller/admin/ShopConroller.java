package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author 26706
 */
@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopConroller {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态为：{}", shopStatus == 1 ? "营业中": "打烊中");
        return Result.success(shopStatus);
    }

    /**
     * 设置店铺状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置店铺状态：{}", status == 1 ? "营业中": "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

}
