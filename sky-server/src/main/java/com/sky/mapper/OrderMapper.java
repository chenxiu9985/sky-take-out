package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 26706
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据状态和下单时间小于查询订单
     * @return
     */
    @Select("select * from sky_take_out.orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 更新超时订单状态
     * @param order
     */
    void update(Orders order);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     *
     * @param id
     */
    @Select("select * from orders where id= #{id}")
    Orders getById(Long id);

    /**
     * 根据动态条件统计订单数量
     *
     * @param map
     */
    Integer countByMap(Map<String, Object> map);

    /**
     * 根据动态条件统计订单金额
     * @param map
     * @return
     */
    Double getStatisticsByTime(Map<String, Object> map);

    /**
     * 根据下单时间查询订单
     * @param beginTime
     * @param endTime
     * @param status
     * @return
     */
    @Select("select * from sky_take_out.orders where order_time between #{beginTime} and #{endTime} and status = #{status}")
    List<Orders> selectByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    /**
     * top10
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计总营业额
     * @return
     */
    @Select("select sum(amount) from sky_take_out.orders where status = 5")
    Double getTotalTurnover();
}
