package com.turntabl.marketdata.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.enums.Exchange;
import com.turntabl.marketdata.enums.Side;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    private Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>> orderbooks;
    public static String message ="";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>> orderbooks;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TypeReference<Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>>> typeRef
                    = new TypeReference<>() {};
            orderbooks = objectMapper.readValue(message.getBody(), typeRef);
            this.orderbooks = orderbooks;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //log.info("Logging the data {}", orderbook);
    }

    public Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>> getOrderbooks() {
        return orderbooks;
    }
}
