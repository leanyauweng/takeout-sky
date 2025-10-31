package com.itheima.service.impl;

import com.itheima.constant.MessageConstant;
import com.itheima.exception.AccountNotFoundException;
import com.itheima.exception.PasswordErrorException;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.dto.UserLoginDTO;
import com.itheima.pojo.entity.User;
import com.itheima.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserMapper userMapper;
    @Override
    public com.itheima.pojo.entity.User login(UserLoginDTO userLoginDTO) {
        String phone = userLoginDTO.getPhone();
        String password = userLoginDTO.getPassword();

        //1.根据用户手机号查询数据库中的数据
        User user = userMapper.getByUserPhone(phone);

        //2.处理各种异常情况（用户手机号不存在，密码不对）
        if(user ==null){
            throw new AccountNotFoundException(MessageConstant.PHONE_NOT_FOUND );
        }

        //密码对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(user.getPassword())){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3.返回实体对象
        return user;

    }
}
