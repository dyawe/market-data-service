package com.turntabl.marketdata.config;

import com.turntabl.marketdata.service.impl.RedisMessageSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.List;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${market-data.variables.redis.topic}")
    private  String topic;
    @Value("${market-data.variables.redis.topic-ex2}")
    private  String topic2;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    Jedis jedis() {
        JedisShardInfo shardInfo = new JedisShardInfo(host, port);
        shardInfo.setPassword(password);
        return new Jedis(shardInfo);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter( new RedisMessageSubscriber(), "onMessage");
    }


    @Bean
    RedisMessageListenerContainer redisContainer( ) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), List.of(topic()));
        return container;
    }

//    @Bean
//    MessagePublisher redisPublisher() {
//        return new RedisMessagePublisher(redisTemplate(), topic(), topicForExchange2());
//    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(topic);
    }


    @Bean
    ChannelTopic topicForExchange2() {
        return new ChannelTopic(topic2);
    }
}
