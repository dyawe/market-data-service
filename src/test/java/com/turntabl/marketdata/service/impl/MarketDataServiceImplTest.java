package com.turntabl.marketdata.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.dto.OrderFromExchange;
import com.turntabl.marketdata.enums.Exchange;
import com.turntabl.marketdata.enums.Side;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest

class MarketDataServiceImplTest {
    @Autowired
    RedisMessagePublisher publisher;

    @Autowired
    RedisMessageSubscriber subscriber;

    @Test
    public void redisPubSubTest(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        OrderBookDto orderBookDto = new OrderBookDto();
        OrderBookDto orderBookDto2 = new OrderBookDto();
        orderBookDto.setAskPrice(50.5f);
        orderBookDto2.setAskPrice(80.5f);
        orderBookDto.setBidPrice(50.5f);
        orderBookDto2.setBidPrice(80.5f);
        orderBookDto.setTicker("aapl");
        orderBookDto2.setTicker("msft");
        orderBookDto.setBuyLimit(100);
        orderBookDto.setBuyLimit(200);

        List list = List.of(orderBookDto, orderBookDto2);

        Map test = new HashMap();
        test.put("tickerClassification", list);
        Map<Side, Map<String, List<OrderBookDto>>> map = new HashMap();
        map.put(Side.BUY, test);
        OrderFromExchange exchange = OrderFromExchange.builder().exchange(Exchange.ONE).orders(map).build();
        publisher.publish(exchange);
        assertEquals(exchange,exchange);

    }


}