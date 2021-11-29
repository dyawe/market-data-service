package com.turntabl.marketdata.service.impl;

import com.turntabl.marketdata.service.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class RedisMessagePublisher implements MessagePublisher {
    private  RedisTemplate<String, Object> redisTemplate;
    private    ChannelTopic topic;

    @Override
    public void publish(String message) {
       redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
