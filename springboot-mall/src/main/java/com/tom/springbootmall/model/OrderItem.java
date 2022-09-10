package com.tom.springbootmall.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {

    private Integer orderItemId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Integer amount;


}
