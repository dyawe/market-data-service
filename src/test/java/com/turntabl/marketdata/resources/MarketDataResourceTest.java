package com.turntabl.marketdata.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turntabl.marketdata.service.impl.MarketDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.turntabl.marketdata.dto.OrderBookDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

class MarketDataResourceTest {
    @Autowired
    private MockMvc mockMvc;

    private OrderBookDto order = new OrderBookDto("AAPL",2,2, 1.5F);
    ObjectMapper Obj = new ObjectMapper();
    String orderBookJsonStr = Obj.writeValueAsString(List.of(order,order));

    MarketDataResourceTest() throws JsonProcessingException {
    }

    @Test
    public void mockTestExample() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/mockito")).andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCallBackFromExchangeOne() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/callback/webhook")
                .content(orderBookJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void testCallBackFromExchangeTwo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/callback2/webhook")
                        .content(orderBookJsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}