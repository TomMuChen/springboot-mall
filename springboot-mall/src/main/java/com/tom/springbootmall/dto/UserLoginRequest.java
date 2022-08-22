package com.tom.springbootmall.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;



@Getter
@Setter
public class UserLoginRequest {

    @NotBlank  //資料不得為空
    @Email //用於檢察email格式
    private String email;
    @NotBlank
    private String password;

}
