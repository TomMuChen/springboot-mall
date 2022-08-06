package com.tom.springbootmall.util;

import lombok.Data;

import java.util.List;

/**
 * 用於產生json OBJ返回給前端
 * @param <T>
 */

@Data //自動產生getter setter
public class Page <T>{
    private Integer limit;
    private Integer offset;
    private Integer total; //總比數
    private List<T> result; //存放Product


}
