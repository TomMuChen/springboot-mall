package com.tom.springbootmall.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


//依照SQL table設計對應的user class
//注意命名部分要修改

@Getter
@Setter
public class User {
    private Integer userId;
    private String email;
    private String password;
    private Date createdDate;
    private Date lastModifiedDate;

}
