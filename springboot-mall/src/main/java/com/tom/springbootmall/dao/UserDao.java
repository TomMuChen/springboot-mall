package com.tom.springbootmall.dao;

import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer id);
    User getUserByEmail(String email);

}
