package com.tom.springbootmall.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank  //資料不得為空
    private String email;
    @NotBlank
    private String password;

}
