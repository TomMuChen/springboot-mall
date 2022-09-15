package com.tom.springbootmall.service.impl;


import com.tom.springbootmall.dao.OrderDao;
import com.tom.springbootmall.dao.ProductDao;
import com.tom.springbootmall.dao.UserDao;
import com.tom.springbootmall.dto.BuyItem;
import com.tom.springbootmall.dto.CreateOrderRequest;
import com.tom.springbootmall.model.Order;
import com.tom.springbootmall.model.OrderItem;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.model.User;
import com.tom.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private static final Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;


    /**
     * 取得訂單
     * @param orderId 訂單ID
     * @return
     */
    @Override
    public Order getOrderById(Integer orderId) {
        log.info("取得編號為 {} 的訂單",orderId);
        Order order=orderDao.getOrderById(orderId);

        //把訂單所有品項置入
        List<OrderItem>orderItemList=orderDao.getOrderItemsById(orderId);
        order.setOrderItemList(orderItemList);
        return order;
    }

    /**
     * 因這邊的訂單資訊分2個table
     * @param userId
     * @param createOrderRequest
     * @return
     */
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        //創建訂單前，先檢查user是否存在，若不存在拋出exception
        User user=userDao.getUserById(userId);
        if (user==null){
            log.info("該userId:{} 不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //從前端回傳的資料，篩出需要的資訊
        int totalAmount=0;
        List<BuyItem> buyItemList=createOrderRequest.getBuyItemList();
        List<OrderItem> orderItemList=new ArrayList<>();
        for (BuyItem item :buyItemList){ //逐個篩出商品細項
            Product product=productDao.getProductById(item.getProductId());

//            檢查商品2個地方 #商品是否存在 #商品次存是否足夠
//            當滿足兩條件時，才能成立訂單
            if (product==null){
                log.info("商品{}不存在",item.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock()<item.getQuantity()) {
                log.info("商品{}庫存數量不足，無法購買。剩餘庫存:{}，欲購買數量:{}",item.getProductId(),product.getStock(),item.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            productDao.updateStock(product.getProductId(),product.getStock()-item.getQuantity());

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
