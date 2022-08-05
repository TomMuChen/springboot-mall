package com.tom.springbootmall.service.impl;

import com.tom.springbootmall.dao.impl.ProductDaoImpl;
import com.tom.springbootmall.dao.productDao;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private productDao productDao;

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


}
