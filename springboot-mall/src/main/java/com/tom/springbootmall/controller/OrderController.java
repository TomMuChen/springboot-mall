package com.tom.springbootmall.controller;


import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.dto.OrderQueryParams;
import com.tom.springbootmall.model.Order;
import com.tom.springbootmall.service.OrderService;
import com.tom.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

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

       //這邊的order應包含細項
       Order order=orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


    //Test
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
                                                 @RequestParam(defaultValue = "0")  @Min(0) Integer offset){

        OrderQueryParams orderQueryParams=new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        //取得order List
        List<Order> orderList=orderService.getOrders(orderQueryParams);

        //取得order總數
        Integer count=orderService.countOrder(orderQueryParams);


        //分頁
        Page<Order>page=new Page<>();
        page.setOffset(offset);
        page.setLimit(limit);
        page.setTotal(count);
        page.setResult(orderList);


//       Order order= orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(page);
    }

}
