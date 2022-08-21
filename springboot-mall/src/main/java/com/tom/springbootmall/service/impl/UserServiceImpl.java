package com.tom.springbootmall.service.impl;

import com.tom.springbootmall.dao.UserDao;
import com.tom.springbootmall.dao.impl.UserDaoImpl;
import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;
import com.tom.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;



    public Integer register(UserRegisterRequest userRegisterRequest){

        return userDao.createUser(userRegisterRequest) ;
    }

    public User getUserById(Integer userId){

        return userDao.getUserById(userId);
    }
}
