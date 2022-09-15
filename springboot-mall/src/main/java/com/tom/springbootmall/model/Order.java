package com.tom.springbootmall.model;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order {

    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;
    private Date createdDate;
    private Date lastModifiedDate;
    private List<OrderItem> OrderItemList;

}
