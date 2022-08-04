package com.tom.springbootmall.rawMapper;

import com.tom.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


// RowMapper<Product>後面用泛型表示是要回傳Product
public class ProductRowMapper implements RowMapper<Product> {

    @Override
    //ResultSet rs -->表示是所抓到的物件
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product=new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setCategory(rs.getString("category"));
        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));
        product.setCreateDate(rs.getTimestamp("created_date"));
        product.setLast_modified_date(rs.getTimestamp("last_modified_date"));
        return product;
    }
}
