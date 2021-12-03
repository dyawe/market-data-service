package com.turntabl.marketdata.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<OrderBookDto> orderbooks = null;
//        try {
//            orderbooks  = objectMapper.readValue(message.getBody(), List.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String topic = new String(message.getChannel(), StandardCharsets.UTF_8);
        log.info("In Message susbcriber service. Logging subscribed message", message.getBody());
    }
}
