package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dao.productDao;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.rowMapper.ProductRowMapper;
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
    public List<Product> getProducts(ProductCategory category, String search) {
        String sql="SELECT product_id,product_name,category,image_url,price,stock,description," +
                "created_date,last_modified_date " +
                "FROM product WHERE 1=1";
        Map<String,Object> map=new HashMap<>();

        if (category!=null){
            sql=sql+" AND category=:category";
            map.put("category",category.name());//這裡要做轉型
        }

        if (search!=null){
            sql=sql+" AND product_name LIKE :search";
            map.put("search","%"+search+"%");//表 %[蘋果% 只要包含蘋果的都要列出
        }

        ProductRowMapper pr=new ProductRowMapper();
        List<Product> productList=npjt.query(sql,map,pr);
        return productList;

    }

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
        String sql="INSERT INTO product(product_name,category,image_url,price,stock,description," +
                "created_date,last_modified_date)" +
                "VALUE(:productName,:category,:imageUrl,:price,:stock,:description,:created_date,:last_modified_date) ";
        Map<String,Object> map=new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());//?
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        //創建當下時間，當作商品創建時間&修改時間
        Date now=new Date();
        map.put("created_date",now);
        map.put("last_modified_date",now);

        KeyHolder keyHolder=new GeneratedKeyHolder();

        //下面這端程式為何?
        npjt.update(sql,new MapSqlParameterSource(map),keyHolder);
        Integer productId=keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql="UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl,price=:price, stock=:stock, description=:description,last_modified_date=:lastModifiedDate WHERE product_id=:productId ";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId); //這邊要注意 篩選值也要放到MAP
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("lastModifiedDate",new Date());

        npjt.update(sql,map);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql="DELETE FROM product WHERE product_id=:productId ";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);
        npjt.update(sql,map);
    }

}
