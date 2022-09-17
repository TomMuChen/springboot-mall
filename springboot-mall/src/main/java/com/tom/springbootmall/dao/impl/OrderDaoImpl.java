package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.OrderDao;
import com.tom.springbootmall.dto.OrderQueryParams;
import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.model.Order;
import com.tom.springbootmall.model.OrderItem;
import com.tom.springbootmall.rowMapper.OrderItemRowMapper;
import com.tom.springbootmall.rowMapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql="SELECT count(*)" +
                " FROM `order`" +
                " WHERE 1=1 ";
        Map<String,Object>map=new HashMap<>();
        sql=addFilteringSql(sql,map,orderQueryParams);
        Integer total=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);
        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql="SELECT *" +
                " FROM `order`" +
                " WHERE 1=1 ";
        Map<String,Object>map=new HashMap<>();

        //查詢條件
        sql=addFilteringSql(sql,map,orderQueryParams);


//        排序
        sql=sql+" ORDER BY created_date DESC";

        //分頁
        sql=sql+" LIMIT :limit OFFSET :offset";
        map.put("limit",orderQueryParams.getLimit());
        map.put("offset",orderQueryParams.getOffset());

        List<Order> orderList=namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());
        return orderList;
    }



    /**
     * 取得訂單明細，sql中會多select product_name,p.image_url給前端
     * @param orderId
     * @return
     */
    @Override

    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        //todo 這邊join的語法好好理解
        String sql="SELECT oi.order_item_id,oi.order_id,oi.product_id,oi.quantity,oi.amount,p.product_name,p.image_url" +
                " FROM order_item as oi" +
                " LEFT JOIN product as p" +
                " ON oi.product_id=p.product_id" +
                " WHERE oi.order_id=:orderId";

//        String sql="SELECT order_item_id,order_id,order_item.product_id,quantity,amount,product_name,image_url" +
//                " FROM order_item " +
//                " LEFT JOIN product " +
//                " ON order_item.product_id=product.product_id" +
//                " WHERE order_item.order_id=:orderId";

        Map<String,Object>map=new HashMap<>();
        map.put("orderId",orderId);
        List<OrderItem> orderItemList=namedParameterJdbcTemplate.query(sql,map,new OrderItemRowMapper());
        return orderItemList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql="SELECT order_id,user_id,total_amount,created_date,last_modified_date " +
                " FROM `order` WHERE order_id=:orderId";



        Map<String,Object>map=new HashMap<>();
        map.put("orderId",orderId);

        List<Order> orderList=namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());
        if (orderList.size()>0){
            return orderList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql="INSERT INTO `order` (user_id,total_amount,created_date,last_modified_date)" +
                " VALUE(:userId,:totalAmount,:createDate,:lastModifiedDate)";

        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        map.put("totalAmount",totalAmount);

        Date now=new Date();
        map.put("createDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder=new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);
        int orderId=keyHolder.getKey().intValue();

        return orderId;
    }


    //這邊要插入的是一個list的東西，可用batchupdate
    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {

        //使用batchupdate
        String sql="INSERT INTO `order_item`(order_id,product_id,quantity,amount)" +
                " VALUE(:orderId,:productId,:quantity,:amount)";
        MapSqlParameterSource[] parameterSources=new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem=orderItemList.get(i);
            parameterSources[i]=new MapSqlParameterSource();
            parameterSources[i].addValue("orderId",orderId);
            parameterSources[i].addValue("productId",orderItem.getProductId());
            parameterSources[i].addValue("quantity",orderItem.getQuantity());
            parameterSources[i].addValue("amount",orderItem.getAmount());

        }
        namedParameterJdbcTemplate.batchUpdate(sql,parameterSources);

    }


    private String addFilteringSql(String sql, Map<String,Object> map, OrderQueryParams orderQueryParams){
        if (orderQueryParams.getUserId()!=null){
            sql=sql+" AND user_id=:userId";
            map.put("userId",orderQueryParams.getUserId());//這裡要做轉型
        }
        return sql;
    }



}
