package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 保存菜品口味
     * @param dishFlavorList
     */
    void insertBatch(List<DishFlavor> dishFlavorList);

    /**
     * 更新菜品口味
     * @param dishFlavorList
     */
    void update(List<DishFlavor> dishFlavorList);

    /**
     * 根据菜品id查询菜品口味
     * @param id
     */
    @Select("select dish_id, name, value from sky_take_out.dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 根据dishId来删除
     * @param id
     */
    @Delete("delete from sky_take_out.dish_flavor where dish_id= #{id}")
    void deleteById(Long id);

    /**
     * 批量删除，会传进不同的dishId
     * @param ids
     */
    void deleteByIds(List<Long> ids);


}
