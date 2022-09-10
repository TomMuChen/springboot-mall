package com.tom.springbootmall.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BuyItem {

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;
}
