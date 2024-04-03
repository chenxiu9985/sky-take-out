package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    Orders getOrderDetailById(Long id);

    /**
     * 提交订单
     * @param submitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO submitDTO);
}
