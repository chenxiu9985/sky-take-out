package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    /**
     * 今日运营数据
     * @return
     */
    BusinessDataVO getbusinessData(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 套餐总览
     * @return
     */
    SetmealOverViewVO getSetmealsOverView();

    /**
     * 菜品总览
     * @return
     */
    DishOverViewVO getDishsOverView();

    /**
     * 订单管理数据
     * @return
     */
    OrderOverViewVO getOrdersOverView();
}
