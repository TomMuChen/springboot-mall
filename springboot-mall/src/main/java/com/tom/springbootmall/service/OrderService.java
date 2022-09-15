package com.tom.springbootmall.service;

import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);
     Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
