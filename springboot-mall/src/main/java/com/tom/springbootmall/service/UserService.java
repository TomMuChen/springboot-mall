package com.tom.springbootmall.service;

import com.tom.springbootmall.dto.UserLoginRequest;
import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;

public interface UserService {

     Integer register(UserRegisterRequest userRegisterRequest);
     User getUserById(Integer id);

     User login (UserLoginRequest userLoginRequest);

}
