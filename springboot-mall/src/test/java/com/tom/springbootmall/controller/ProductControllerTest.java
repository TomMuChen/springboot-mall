package com.tom.springbootmall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.springbootmall.constant.ProductCategory;
import com.tom.springbootmall.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc  //加這註解後，可使用MockMvc 這個Bean
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper=new ObjectMapper();
    Logger log= LoggerFactory.getLogger(ProductControllerTest.class);

//查詢商品
    @Test
    public  void getProducts() throws Exception {
        RequestBuilder rb= MockMvcRequestBuilders.get("/products/{productId}",1);
        mockMvc.perform(rb)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", notNullValue()))
                .andExpect(jsonPath("$.stock", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.createDate", notNullValue()))
                .andExpect(jsonPath("$.last_modified_date", notNullValue()));
    }

    @Test
    public void getProduct_notFound() throws Exception {

        RequestBuilder rb=MockMvcRequestBuilders.get("/products/{productId}",279);
        mockMvc.perform(rb)
                .andExpect(status().isNotFound())//上下兩段寫法是相同的
                .andExpect(status().is(404));

    }



}