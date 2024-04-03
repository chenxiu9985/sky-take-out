package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("根据页面查询：{}", employeePageQueryDTO);
        PageResult<Employee> employee = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(employee);
    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable String id) {
        log.info("根据id查询员工，{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
        public Result<String> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码，初始密码为{}，新密码为{}", passwordEditDTO.getOldPassword(), passwordEditDTO.getNewPassword());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 添加员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {

        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 启用、禁用员工账号
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    public Result changeStatus(Long id, @PathVariable Integer status) {
        log.info("启用、禁用员工账号：{} {}", id, status);
        employeeService.changeStatus(id, status);
        return Result.success();
    }

}
