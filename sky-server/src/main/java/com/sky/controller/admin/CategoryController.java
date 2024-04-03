package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 更新分类
     * @param categoryDTO
     * @return
     */
    @PutMapping()
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("更新菜单：{}", categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult =categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 更新分类状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result changeStatus(Long id, @PathVariable Integer status){
        log.info("更新分类状态：{}，{}", id, status);
        categoryService.changeStatus(id, status);
        return Result.success();
    }

    /**
     * 添加分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("添加分类：{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result deleteCategory(Long id){
        log.info("删除分类：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> queryByType(Integer type){
        log.info("根据类型查询分类：{}", type);
        List<Category> categorylist = categoryService.queryByType(type);
        return Result.success(categorylist);
    }


}
