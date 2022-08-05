package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.productDao;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.rawMapper.ProductRowMapper;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="INSERT INTO product(product_name,category,image_url,price,stock,description,created_date,last_modified_date)" +
                "VALUE(:productName,:category,:imageUrl,:price,:stock,:description,:created_date,:last_modified_date) ";
        Map<String,Object> map=new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now=new Date();
        map.put("created_date",now);
        map.put("last_modified_date",now);

        KeyHolder keyHolder=new GeneratedKeyHolder();

        //下面這端程式為何?
        npjt.update(sql,new MapSqlParameterSource(),keyHolder);
        Integer productId=keyHolder.getKey().intValue();

        return productId;
    }

}
