package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 26706
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 购物车展示
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 数量加一
     */
    @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart shoppingCart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert("insert into sky_take_out.shopping_cart(user_id,dish_id,setmeal_id,dish_flavor,number,amount,name,image)" +
            "values (#{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{name}, #{image})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 展示购物车里面的东西
     * @return
     */
    @Select("select * from sky_take_out.shopping_cart;")
    List<ShoppingCart> listAll();

    /**
     * 删除一条数据
     * @param id
     */
    @Delete("delete from sky_take_out.shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 清空购物车
     * @param currentId
     */
    @Delete("delete from sky_take_out.shopping_cart where user_id = #{currentId}")
    void cleanById(Long currentId);

}
