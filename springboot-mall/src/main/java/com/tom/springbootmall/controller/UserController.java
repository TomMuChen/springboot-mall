package com.tom.springbootmall.controller;


import com.tom.springbootmall.dto.UserRegisterRequest;
import com.tom.springbootmall.model.User;
import com.tom.springbootmall.service.UserService;
import com.tom.springbootmall.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 為了管理方便，在useru,也設計一個MVC三層
 */

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //註冊新帳號
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody  @Valid UserRegisterRequest userRegisterRequest){
        //創建成功後，回傳id值
        Integer id= userService.register(userRegisterRequest);

        //藉由回傳的id值去抓出資料
        User user=userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
