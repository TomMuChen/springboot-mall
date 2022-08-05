package com.tom.springbootmall.dao;

import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;

public interface productDao {

     Product getProductById(Integer productId);
     Integer createProduct(ProductRequest productRequest);
     void updateProduct(Integer productId,ProductRequest productRequest);
     void deleteProductById(Integer productId);
}
