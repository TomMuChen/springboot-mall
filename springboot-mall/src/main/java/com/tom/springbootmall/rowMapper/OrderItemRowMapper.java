package com.tom.springbootmall.rowMapper;

import com.tom.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    /**
     * 這個rowmapper較特別，因為sql在抓資料料時，有join其他table
     * #事實上。rowmapper`
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem=new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setAmount(rs.getInt("amount"));

        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setImageUrl(rs.getString("image_url"));
        return orderItem;
    }
}
