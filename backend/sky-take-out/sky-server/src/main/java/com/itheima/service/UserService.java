package com.itheima.service;

import com.itheima.pojo.dto.UserLoginDTO;
import com.itheima.pojo.entity.User;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
