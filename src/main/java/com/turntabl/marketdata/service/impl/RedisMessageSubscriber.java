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


import java.nio.charset.StandardCharsets;
@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {


    private static String messageString ="";

    public static String getMessageString() {
        return messageString;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
    String topic = new String(message.getChannel(), StandardCharsets.UTF_8);
    messageString = message.toString();
    }

//    public Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>> getOrderbooks() {
//        return orderbooks;
//    }
}
