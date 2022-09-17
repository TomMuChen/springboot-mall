package com.tom.springbootmall.service;

import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.dto.OrderQueryParams;
import com.tom.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Order getOrderById(Integer orderId);
     Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);
}
