package com.tom.springbootmall.controller;

import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required=false)String search){

        //參數統一管理
        ProductQueryParams productQueryParams=new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);

        List<Product> productList=productService.getProducts(productQueryParams);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }


    @GetMapping("/products/{productId}")
    public ResponseEntity<Product>getProduct(@PathVariable Integer productId){
        Product product= productService.getProductById(productId);

        if (product!=null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //ProductRequest特別新增了
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        //呼叫service層並傳入json資料{productRequest}
        Integer  productId=productService.createProduct(productRequest);
        Product product=productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){

        //檢查商品是否存在
        Product product=productService.getProductById(productId);
        if (product==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        //修改商品數據
        productService.updateProduct(productId,productRequest);
        Product updatedProduct=productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId ){
        productService.deleteProductById(productId);


        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
        //這邊的Response 返回方法怎麼用的?
    }

}
