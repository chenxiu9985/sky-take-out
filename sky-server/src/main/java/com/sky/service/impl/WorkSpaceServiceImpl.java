package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 26706
 */
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 今日运营数据
     * @return
     */
    public BusinessDataVO getbusinessData(LocalDateTime beginTime, LocalDateTime endTime) {
        //营业额
        Double turnover = orderMapper.getTotalTurnover() == null ? 0.0 : orderMapper.getTotalTurnover();

        //总订单
        Integer totalOrder = getCountByStatus(null);

        //有效订单
        Integer validOrder = getCountByStatus(Orders.COMPLETED);

        //订单完成率
        Double orderCompletionRate = totalOrder == 0 ? 0 : (double) validOrder / totalOrder;
        //两位小数
        Double roundedOrderCompletionRate = Double.parseDouble(String.format("%.2f", orderCompletionRate));

        //平均客单价
        Double avgPrice = totalOrder == 0 ? 0 : turnover / validOrder;
        //两位小数
        Double roundedAvgPrice = Double.parseDouble(String.format("%.2f", avgPrice));

        //新增用户数量
        Map<String, Object> mapTime = new HashMap<>();
        mapTime.put("end", endTime);
        mapTime.put("begin", beginTime);

        Integer newUsers = userMapper.getCountByTime(mapTime);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrder)
                .orderCompletionRate(roundedOrderCompletionRate)
                .unitPrice(roundedAvgPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * 套餐总览
     * @return
     */
    public SetmealOverViewVO getSetmealsOverView() {
        return SetmealOverViewVO.builder()
                .sold(setmealMapper.countByStatus((Integer)1))
                .discontinued(setmealMapper.countByStatus((Integer)0))
                .build();
    }

    /**
     * 菜品总览
     * @return
     */
    public DishOverViewVO getDishsOverView() {
        return DishOverViewVO.builder()
                .sold(dishMapper.countByStatus((Integer)1))
                .discontinued(dishMapper.countByStatus((Integer)0))
                .build();
    }

    /**
     * 订单管理数据
     * @return
     */
    public OrderOverViewVO getOrdersOverView() {
        return OrderOverViewVO.builder()
                .waitingOrders(getCountByStatus(Orders.TO_BE_CONFIRMED))
                .deliveredOrders(getCountByStatus(Orders.CONFIRMED))
                .completedOrders(getCountByStatus(Orders.COMPLETED))
                .cancelledOrders(getCountByStatus(Orders.CANCELLED))
                .allOrders(getCountByStatus(null))
                .build();
    }

    /**
     * 根据状态获取订单数量
     * @param status
     * @return
     */
    private Integer getCountByStatus(Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return orderMapper.countByMap(map);
    }
}
