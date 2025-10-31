package com.itheima.controller.admin;

import com.itheima.constant.JwtClaimsConstant;
import com.itheima.pojo.dto.ChangePasswordDTO;
import com.itheima.pojo.dto.EmployeeDTO;
import com.itheima.pojo.dto.EmployeeLoginDTO;
import com.itheima.pojo.dto.EmployeePageQueryDTO;
import com.itheima.pojo.entity.Employee;
import com.itheima.pojo.vo.EmployeeLoginVO;
import com.itheima.properties.JwtProperties;
import com.itheima.result.PageResult;
import com.itheima.service.EmployeeService;
import com.itheima.result.Result;
import com.itheima.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工相关接口", description = "管理员员工登录、管理相关接口")
public class EmployeeController {
    @Autowired private EmployeeService employeeService;
    @Autowired private JwtProperties jwtProperties;

    @PostMapping("/login")
    @Operation(summary = "员工登录", description = "管理员或员工登录获取 JWT")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDT0){
        log.info("员工登录:{}",employeeLoginDT0);

        Employee employee = employeeService.login(employeeLoginDT0);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
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

    @PutMapping("/updatePassword")
    @Operation(summary = "修改员工密码")
    public Result updatePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        employeeService.changePassword(changePasswordDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    @Operation(summary = "员工退出")
    public  Result<String> logout(){ return Result.success(); }

    @PostMapping
    @Operation(summary = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "员工分页查询")
    public Result<PageResult> page(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize){

        EmployeePageQueryDTO employeePageQueryDTO = new EmployeePageQueryDTO();
        employeePageQueryDTO.setName(name);
        employeePageQueryDTO.setPage(page);
        employeePageQueryDTO.setPageSize(pageSize);

        log.info("员工分页查询，参数：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号：{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询员工")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @Operation(summary = "编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
