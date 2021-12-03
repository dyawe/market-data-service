package com.turntabl.marketdata.service.impl;

import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.exceptions.AlreadySubscribedException;
import com.turntabl.marketdata.service.MarketDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarketDataServiceImpl implements MarketDataService {
    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    RedisMessageSubscriber redisMessageSubscriber;
    @Value("${market-data.variables.exchange-url.one}")
    private String exchange1Url;
    @Value("${market-data.variables.exchange-url.two}")
    private String exchange2Url;
    @Value("${market-data.variables.callback}")
    private String callback;
    @Value("${market-data.variables.callback-ex2}")
    private String exchange2Callback;

    @Override
    public List<OrderBookDto> getOrderBooks() {
        return restTemplate.exchange(exchange1Url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OrderBookDto>>(){}).getBody();

    }

    @Override
    public List<String> getSubscriptions() {
        return restTemplate.exchange(exchange1Url + "/subscription", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() { }).getBody();
    }

    @Override
    public void subscribe() throws AlreadySubscribedException {

        if(getSubscriptions().contains(callback)) {
            log.info("======>Exception thrown. You have already subscribed with this endpoint<=========================");
            throw new AlreadySubscribedException(callback);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(callback, httpHeaders);
        HttpEntity<?> httpEntityEx2 = new HttpEntity<>(exchange2Callback, httpHeaders);

        restTemplate.exchange(exchange1Url + "/subscription", HttpMethod.POST, httpEntity, String.class);
        log.info("======>Successfully subscribed to exchange one. Waiting for market data======>");
        restTemplate.exchange(exchange2Url + "/subscription", HttpMethod.POST, httpEntityEx2, String.class);
        log.info("======>Successfully subscribed to exchange two. Waiting for market data======>");

    }

    @Override
    public Boolean unsubscribe() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(callback, httpHeaders);
        return restTemplate
                .exchange(exchange1Url+ "/subscription", HttpMethod.DELETE, httpEntity, Boolean.class).getBody();
    }

    @Override
    public void mutateSubscription() {

    }

}
