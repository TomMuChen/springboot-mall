package com.tom.springbootmall.dao;

import com.tom.springbootmall.model.Order;
import com.tom.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    List<OrderItem> getOrderItemsById(Integer orderId);
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId,Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
