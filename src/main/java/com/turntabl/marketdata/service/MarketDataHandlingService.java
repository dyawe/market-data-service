package com.turntabl.marketdata.service;

import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.enums.Exchange;
import com.turntabl.marketdata.enums.Side;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MarketDataHandlingService {
    Map<Side, Map<String, List<OrderBookDto>>> transformData(List<OrderBookDto> orderBookDtos);
    void receiveOrderBooks(List<OrderBookDto> orderbooks, Exchange type);
}
