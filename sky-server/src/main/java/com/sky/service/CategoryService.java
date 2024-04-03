package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 更新分类
     * @param categoryDTO
     * @return
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新分类状态
     * @param id
     * @param status
     * @return
     */
    void changeStatus(Long id, Integer status);

    /**
     * 添加分类
     * @param categoryDTO
     * @return
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> queryByType(Integer type);
}
