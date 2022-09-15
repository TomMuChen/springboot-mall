package com.tom.springbootmall.dao.impl;

import com.tom.springbootmall.dao.ProductDao;
import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.rowMapper.ProductRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProductDaoImpl implements ProductDao {
    public static final Logger log= LoggerFactory.getLogger(ProductDaoImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate npjt;


    /***用於計算商品總數*/
    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql="SELECT COUNT(*) FROM product WHERE 1=1 ";
        Map<String,Object> map=new HashMap<>();

        /*** 查詢條件*/

        //運用拼接方式設置SQL 語法
        sql=addFilteringSql(sql,map,productQueryParams);
        //這段被替代
//        if (productQueryParams.getCategory()!=null){
//            sql=sql+" AND category=:category";
//            map.put("category",productQueryParams.getCategory().name());//這裡要做轉型
//        }
//        if (productQueryParams.getSearch()!=null){
//            sql=sql+" AND product_name LIKE :search";
//            map.put("search","%"+productQueryParams.getSearch()+"%");//表 %[蘋果% 只要包含蘋果的都要列出
//        }

        Integer total=npjt.queryForObject(sql,map,Integer.class);//將SQL中COUNT值轉成Integer返回
        return total;
    }


    /***取得查詢商品列表*/
    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        //設置SQL語法
        String sql="SELECT product_id,product_name,category,image_url,price,stock,description," +
                "created_date,last_modified_date " +
                "FROM product WHERE 1=1";
        Map<String,Object> map=new HashMap<>();

        /*** 查詢條件*/
        //運用拼接方式設置SQL 語法

        sql=addFilteringSql(sql,map,productQueryParams);
        //這段被替代
//        if (productQueryParams.getCategory()!=null){
//            sql=sql+" AND category=:category";
//            map.put("category",productQueryParams.getCategory().name());//這裡要做轉型
//        }
//        if (productQueryParams.getSearch()!=null){
//            sql=sql+" AND product_name LIKE :search";
//            map.put("search","%"+productQueryParams.getSearch()+"%");//表 %[蘋果% 只要包含蘋果的都要列出
//        }

        /*** 排序*/
        sql=sql+" ORDER BY "+productQueryParams.getOrderBy()+" "+productQueryParams.getSort();
        //JDBC中無法用下列方式寫SQL語法  要注意
        //sql=sql+" ORDER BY :orderBy :sort";
        //map.put("orderBy",productQueryParams.getOrderBy());
        //  map.put("sort",productQueryParams.getSort());

        /*** 分頁*/
        //不用設置if null的原因:因在controller已設置了defaultValue，故即便前端沒資料也有預設的資料
        sql=sql+" LIMIT :limit OFFSET :offset";
        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());


        ProductRowMapper pr=new ProductRowMapper();
        List<Product> productList=npjt.query(sql,map,pr);
        return productList;

    }

    /***以商品ID查詢商品*/
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

    //用於更新產品庫存
    @Override
    public void updateStock(Integer productId, Integer stock) {

        String sql="UPDATE product " +
                " SET stock=:stock,last_modified_date=:lastModifiedDate " +
                " WHERE product_id=:productId ";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId); //這邊要注意 篩選值也要放到MAP
        map.put("stock",stock); //這邊要注意 篩選值也要放到MAP
        map.put("lastModifiedDate",new Date());
        log.info("扣除商品庫存: 餘{}",stock);
        npjt.update(sql,map);

    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql="DELETE FROM product WHERE product_id=:productId ";
        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);
        npjt.update(sql,map);
    }


    private String addFilteringSql(String sql, Map<String,Object> map,ProductQueryParams productQueryParams){
        if (productQueryParams.getCategory()!=null){
            sql=sql+" AND category=:category";
            map.put("category",productQueryParams.getCategory().name());//這裡要做轉型
        }
        if (productQueryParams.getSearch()!=null){
            sql=sql+" AND product_name LIKE :search";
            map.put("search","%"+productQueryParams.getSearch()+"%");//表 %[蘋果% 只要包含蘋果的都要列出
        }
        return sql;
    }

}
