package com.tom.springbootmall.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 此類別用於接住前端回傳回的參數
 * ps:這邊是依照前端所回傳的參數而設計的類別
 */

@Getter
@Setter
public class CreateOrderRequest {


    @NotEmpty
    private List<BuyItem> buyItemList;

}
