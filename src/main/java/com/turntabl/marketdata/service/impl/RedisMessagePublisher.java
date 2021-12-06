package com.turntabl.marketdata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.dto.OrderFromExchange;
import com.turntabl.marketdata.enums.Side;
import com.turntabl.marketdata.service.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RedisMessagePublisher implements MessagePublisher {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ChannelTopic topic;

    @Autowired
    ChannelTopic topicForExchange2;

    @Override
    public void publish(OrderFromExchange message) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String messageString = null;
        try {
            log.info("===> Serializing list of order books from exchange one ===>");
            messageString = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(topic.getTopic(), messageString);
            log.info("===> Exchange one publishing order books on channel: {}===>", topic.getTopic());
        } catch (JsonProcessingException ex) {
            log.info("JsonProcessingException occurred while serializing order books from exchange one: channel {} ==>", topic.getTopic());
        }

    }

    public void publish2(String message) {
        log.info("sending from topic two");
        redisTemplate.convertAndSend(topicForExchange2.getTopic(), message);
    }
}
