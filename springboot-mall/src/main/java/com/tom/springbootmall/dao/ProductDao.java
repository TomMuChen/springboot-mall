package com.tom.springbootmall.dao;

import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;

import java.util.List;


public interface ProductDao {

     Integer countProduct(ProductQueryParams productQueryParams);

     List<Product> getProducts(ProductQueryParams productQueryParams);
     Product getProductById(Integer productId);
     Integer createProduct(ProductRequest productRequest);
     void updateProduct(Integer productId,ProductRequest productRequest);
     void deleteProductById(Integer productId);
}
