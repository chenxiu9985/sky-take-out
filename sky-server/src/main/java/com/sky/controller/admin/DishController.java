package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询, {}", dishPageQueryDTO);
        PageResult<DishVO> page = dishService.page(dishPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类id查询菜品,{}", categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品,{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result saveDish(@RequestBody DishDTO dishDTO){
        //往数据库里面添加
        log.info("新增菜品,{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //直接清除缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id){
        log.info("菜品起售、停售,{}, {}", status, id);
        dishService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("更新菜品,{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //直接清除缓存
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {    //SpringMVC框架自动解析
        log.info("批量删除");
        dishService.deleteBatch(ids);
        //直接清除缓存
        cleanCache("dish_*");

        return Result.success();
    }
}
