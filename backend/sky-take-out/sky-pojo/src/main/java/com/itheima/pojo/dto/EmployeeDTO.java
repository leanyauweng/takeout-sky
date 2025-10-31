package com.itheima.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;
    private String name;
    private String username;
    //private String password;
    private String phone;
    private String sex;
    private String idNumber;

}
