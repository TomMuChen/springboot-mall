package com.tom.springbootmall.controller;


import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/users/{userId}/orders")
    //因訂單一定是先有帳號才能創建，故以上述方式，更能呈現訂單與帳號的關係
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        //先寫問號，後面才回來改
       Integer orderId= orderService.createOrder(userId,createOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }


}
