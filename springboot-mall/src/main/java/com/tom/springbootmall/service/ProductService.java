package com.tom.springbootmall.service;

import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;

import java.util.List;

public interface ProductService {


    List<Product> getProducts(ProductCategory category,String search);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProductById(Integer productId);


}
