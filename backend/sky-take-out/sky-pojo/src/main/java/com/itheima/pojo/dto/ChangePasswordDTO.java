package com.itheima.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
