package com.turntabl.marketdata.service.impl;

import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.service.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RedisMessagePublisher implements MessagePublisher {
    private final   RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
//    private final Channel topic;

    @Override
    public void publish(List<OrderBookDto> message) {

       redisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void publish2(String message) {
        redisTemplate.convertAndSend("pubsub:market-data-ex2", message);
    }
}
