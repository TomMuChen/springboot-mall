package com.tom.springbootmall.dto;

import com.tom.springbootmall.constant.ProductCategory;
import lombok.Data;


/**
 * 將Controller中的參數都拉出來統一管理，提高維護性/可讀性
 */
@Data
public class ProductQueryParams {
    private ProductCategory category;
    private String search;


}
