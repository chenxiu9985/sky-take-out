package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.entity.User;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 26706
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对,md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //1、开启分页插件, 也是基于ThreadLocal的，这里放进去
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //2、调用mapper
        List<Employee> employees = employeeMapper.pageQurry(employeePageQueryDTO.getName());
        //3、封装分页结果
        Page<Employee> page = (Page<Employee>) employees;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Employee getById(String id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("*********");
        return employee;
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        String oldPassword = passwordEditDTO.getOldPassword();
        String newPassword = passwordEditDTO.getNewPassword();

        //条件判断
        if (!Objects.equals(oldPassword, newPassword) && !newPassword.isEmpty()) {
            employeeMapper.changePassword(passwordEditDTO.getEmpId(), newPassword);
        }
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //对象转换，属性拷贝(前 --> 后属性必须一致)
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        //补充员工信息，通过常量的方式
        //默认密码 123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //默认状态 启动 1
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(String id) {
        employeeMapper.deteleteEmployee(id);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        Long currentId = BaseContext.getCurrentId();
        Employee employee = Employee.builder().id(id)
                .status(status)
                .build();
        employeeMapper.update(employee);
    }

}
