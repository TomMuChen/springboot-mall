package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.productDao;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.rawMapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements productDao {

    @Autowired
    private NamedParameterJdbcTemplate npjt;


    @Override
    public Product getProductById(Integer productId) {
        String sql="SELECT product_id,product_name,category,image_url,price,stock,description," +
                "created_date,last_modified_date " +
                "FROM product WHERE product_id=:productId";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);

        ProductRowMapper pr=new ProductRowMapper();
        //RowMapper 用於抓取詢後的資料，也一定要做
        List<Product> productList = npjt.query(sql, map, pr);
        if (productList.size()>0){
            return productList.get(0);
        }else {
            return null;
        }
    }
}
