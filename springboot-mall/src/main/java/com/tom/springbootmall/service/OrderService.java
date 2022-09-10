package com.tom.springbootmall.service;

import com.tom.springbootmall.dto.CreateOrderRequest;

public interface OrderService {

     Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
