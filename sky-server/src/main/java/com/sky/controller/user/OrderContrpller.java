package com.sky.controller.user;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@RequestMapping("/user/order")
public class OrderContrpller {

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result getOrderDetail(@PathVariable("id") Long id){
        log.info("查询订单详情,{}", id);
        Orders orders = orderService.getOrderDetailById(id);
        return Result.success();
    }

    /**
     * 提交订单
     * @param submitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO submitDTO){
        log.info("提交订单,{}", submitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(submitDTO);
        return Result.success(orderSubmitVO);
    }


}
