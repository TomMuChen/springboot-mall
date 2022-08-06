package com.tom.springbootmall.service.impl;

import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dao.productDao;
import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private productDao productDao;


    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }
    @Override
    public Product getProductById(Integer productId) {
        Product product=productDao.getProductById(productId);

        return product;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        return  productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct( productId,productRequest);

    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }



}
