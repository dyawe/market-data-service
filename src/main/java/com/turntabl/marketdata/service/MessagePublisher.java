package com.turntabl.marketdata.service;

import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.dto.OrderFromExchange;
import com.turntabl.marketdata.enums.Side;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface MessagePublisher {
    void publish(final OrderFromExchange message);
}
