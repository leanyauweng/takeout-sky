package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.PasswordConstant;
import com.itheima.constant.StatusConstant;
import com.itheima.context.BaseContext;
import com.itheima.exception.AccountLockedException;
import com.itheima.exception.AccountNotFoundException;
import com.itheima.exception.PasswordErrorException;
import com.itheima.mapper.EmployeeMapper;
import com.itheima.pojo.dto.ChangePasswordDTO;
import com.itheima.pojo.dto.EmployeeDTO;
import com.itheima.pojo.dto.EmployeeLoginDTO;
import com.itheima.pojo.dto.EmployeePageQueryDTO;
import com.itheima.pojo.entity.Employee;
import com.itheima.result.PageResult;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired private EmployeeMapper employeeMapper;
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1.根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2.处理各种异常情况（用户名不存在，密码不对，账号被锁定）
        if(employee ==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(employee.getPassword())){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if(employee.getStatus() == StatusConstant.DISABLE){
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3.返回实体对象
        return employee;

    }



    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //拷贝属性
        BeanUtils.copyProperties(employeeDTO,employee);
        //账号状态
        employee.setStatus(StatusConstant.ENABLE);
        //设置密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //设置当前记录人创建人和修改人的id
        //***后期需要改动
        //employee.setCreateUser(BaseContext.getCurrentId());
       // employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        List<Employee> list = employeeMapper.pageQuery(employeePageQueryDTO);
        Page<Employee> page = (Page<Employee>) list;

        long total = page.getTotal();
        List<Employee> result = page.getResult();

        log.info("分页总数 total = {}", total);
        log.info("查询结果 size = {}", result.size());
        log.info("查询结果 records = {}", result);

        return new PageResult(total,result);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Long empId = BaseContext.getCurrentId();
        Employee employee = employeeMapper.getById(empId);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 校验旧密码
        String oldPwdMd5 = DigestUtils.md5DigestAsHex(changePasswordDTO.getOldPassword().getBytes());
        if (!oldPwdMd5.equals(employee.getPassword())) {
            throw new AccountNotFoundException(MessageConstant.OLD_PASSWORD_ERROR);
        }

        // 校验新密码一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new AccountNotFoundException(MessageConstant.PASSWORD_MISMATCH);
        }

        // 更新密码
        String newPwdMd5 = DigestUtils.md5DigestAsHex(changePasswordDTO.getNewPassword().getBytes());
        employee.setPassword(newPwdMd5);
        employeeMapper.update(employee);

        log.info("员工[{}] 修改密码成功", empId);
    }
}
