package com.tom.springbootmall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore //當json再返回值時，會忽略這個屬性
    private String password;

    private Date createdDate;
    private Date lastModifiedDate;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
