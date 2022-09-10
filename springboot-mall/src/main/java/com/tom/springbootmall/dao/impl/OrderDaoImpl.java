package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.OrderDao;
import com.tom.springbootmall.model.OrderItem;
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
            parameterSources[i].addValue("quantity",orderItem.getProductId());
            parameterSources[i].addValue("amount",orderItem.getAmount());

        }
        namedParameterJdbcTemplate.batchUpdate(sql,parameterSources);

    }


}
