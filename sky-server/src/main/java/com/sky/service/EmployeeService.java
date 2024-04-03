package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

/**
 * @author 26706
 */
public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    Employee getById(String id);

    void editPassword(PasswordEditDTO passwordEditDTO);

    void save(EmployeeDTO employeeDTO);

    void updateEmployee(EmployeeDTO employeeDTO);

    void deleteEmployee(String id);

    void changeStatus(Long id, Integer status);
}
