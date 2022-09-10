package com.tom.springbootmall.service.impl;


import com.tom.springbootmall.dao.OrderDao;
import com.tom.springbootmall.dao.ProductDao;
import com.tom.springbootmall.dto.BuyItem;
import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.model.OrderItem;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    /**
     * 因這邊的訂單資訊分2個table
     * @param userId
     * @param createOrderRequest
     * @return
     */

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {


        //從前端回傳的資料，篩出需要的資訊
        int totalAmount=0;
        List<BuyItem> buyItemList=createOrderRequest.getBuyItemList();
        List<OrderItem> orderItemList=new ArrayList<>();
        for (BuyItem item :buyItemList){
            Product product=productDao.getProductById(item.getProductId());

            //取得訂單總價格
            int amount=product.getPrice()*item.getQuantity();
            totalAmount+=amount;

            //把前端資料轉成OrderItem
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId= orderDao.createOrder(userId,totalAmount);

        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
