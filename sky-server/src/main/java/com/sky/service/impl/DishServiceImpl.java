package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 26706
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 分页查询菜品信息
     *
     * @return
     */
    public PageResult<DishVO> page(DishPageQueryDTO dishPageQueryDTO) {
        //使用pageHelper来实现自动分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).build();
        return dishMapper.listQuery(dish);
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO = new DishVO();
        //分别查询dish表和dish_flavor表，并将查询结果封装到dishVO中
        BeanUtils.copyProperties(dishMapper.getById(id), dishVO);
        dishVO.setFlavors(dishFlavorMapper.getByDishId(id));
        return dishVO;
    }

    /**
     * 更新菜品状态
     *
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = Dish.builder().id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }


    /**
     * 更新菜品信息
     * @param dishDTO
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //将前端接受到的信息分开处理, 先更新dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //通过删除+插入的方式来更新
        dishFlavorMapper.deleteById(dishDTO.getId());

        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && dishFlavorList.size() > 0){
            dishFlavorList.forEach(dishFlavor ->{
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(dishFlavorList);
        }
    }

    /**
     * 新增菜品
     *
     * @param dishVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDTO dishDTO) {
        //将前端接受到的信息分开处理
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //插入1条dish数据
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && dishFlavorList.size() > 0) {
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(dishFlavorList);
        }
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        //菜品是否起售
        for (Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                //商品起售中，不能查询
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //是否与套餐相关联
        List<SetmealDish> setmealDishList = setmealMapper.getSetmealIdsByDishIds(ids);
        if(setmealDishList!=null && setmealDishList.size() > 0){
            // 当前菜品被关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL) ;
        }

        //删除操作
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByIds(ids);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.listQuery(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
