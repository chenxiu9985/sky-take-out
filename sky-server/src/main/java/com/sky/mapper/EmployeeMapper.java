package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.vo.EmployeeLoginVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username =#{username}")
    Employee getByUsername(String username);

    /**
     * 分页查询
     * @param name
     * @return
     */
    List<Employee> pageQurry(String name);

    /**
     * 更新数据
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);


    @Select("select id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user from sky_take_out.employee where id = #{id}")
    Employee getById(String id);

    @Update("update sky_take_out.employee set password = #{newPassword} where id = #{id}")
    void changePassword(Long id, String newPassword);



    @Insert("insert into sky_take_out.employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            " values(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    @Update("update sky_take_out.employee set name = #{name}, username = #{username}, phone = #{phone}, sex = #{sex}, id_number = #{idNumber}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateEmployee(Employee employee);

    @Delete("delete from sky_take_out.employee where id=#{id}")
    void deteleteEmployee(String id);

}
