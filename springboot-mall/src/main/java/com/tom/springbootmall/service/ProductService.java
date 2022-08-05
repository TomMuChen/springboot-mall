package com.tom.springbootmall.service;

import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;

public interface ProductService {

    public Product getProductById(Integer productId);
    public  Integer createProduct(ProductRequest productRequest);
}
