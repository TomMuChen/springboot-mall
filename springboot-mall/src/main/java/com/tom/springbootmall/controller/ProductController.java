package com.tom.springbootmall.controller;

import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dto.ProductQueryParams;
import com.tom.springbootmall.dto.ProductRequest;
import com.tom.springbootmall.model.Product;
import com.tom.springbootmall.service.ProductService;
import com.tom.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated  //在controller中加了@Max @Min 則要在加上此註解
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(  //List<Product>
            //查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required=false)String search,

            //排序 sort
            @RequestParam(defaultValue = "created_date")String orderBy,// 當使用者未輸入，則預設為"created_date" 欄位
            @RequestParam(defaultValue = "desc")String sort, //降序排序(由高到低)

            //分頁pagination
            @RequestParam(defaultValue="5")@Max(1000) @Min(0) Integer limit, //取的幾筆商品數據；這邊用了@Max,@Min  註解要再加上@Validated
            @RequestParam(defaultValue="0") @Min(0)Integer offset  //跳過n比商品數據

    ){

        //參數統一管理，在Controll層直接輸入前端傳來的參數，service dao層皆可收到，提高維護性
        ProductQueryParams productQueryParams=new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        //取得productList  //亦可直接以List 型式版回前端
        List<Product> productList=productService.getProducts(productQueryParams);


        //取得商品總數
        //如何計算商品總筆數?
        Integer total=productService.countProduct(productQueryParams);

        //分頁
        //將查詢結果從新打包(以物件型態打包)回傳前端
        Page page=new Page<Product>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(productList);


        return ResponseEntity.status(HttpStatus.OK).body(page);
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
