package com.itheima.service;

import com.itheima.pojo.dto.ChangePasswordDTO;
import com.itheima.pojo.dto.EmployeeDTO;
import com.itheima.pojo.dto.EmployeeLoginDTO;
import com.itheima.pojo.dto.EmployeePageQueryDTO;
import com.itheima.pojo.entity.Employee;
import com.itheima.result.PageResult;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO);
}
