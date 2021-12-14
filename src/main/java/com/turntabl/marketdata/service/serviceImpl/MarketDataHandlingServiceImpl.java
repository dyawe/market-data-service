package com.turntabl.marketdata.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turntabl.marketdata.dto.OrderBookDto;
import com.turntabl.marketdata.dto.OrderFromExchange;
import com.turntabl.marketdata.dto.OrderProcessObject;
import com.turntabl.marketdata.dto.TickerWithOrders;
import com.turntabl.marketdata.enums.Exchange;
import com.turntabl.marketdata.enums.Side;
import com.turntabl.marketdata.service.MarketDataHandlingService;
import com.turntabl.marketdata.service.impl.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketDataHandlingServiceImpl implements MarketDataHandlingService {
    @Autowired
    RedisMessagePublisher redisMessagePublisher;

    @Autowired
    EmitterServiceImpl emitterService;

    @Override
    public Map<Side, Map<String, List<OrderBookDto>>> transformData(List<OrderBookDto> orderBooks) {

        Map<Side, Map<String, List<OrderBookDto>>> ordersGroupedBySide = new HashMap<>();

        var sortByAskPriceDescending = sortByAskPriceDescending(orderBooks);

//        var  dd = sortByAskPriceDescending.entrySet().stream()
//                .map(x -> TickerWithOrders.builder()
//                        .ticker(x.getKey())
//                        .orders(x.getValue())
//                        .build())
//               .map(tickerWithOrders -> OrderProcessObject.builder()
//                       .tickerWithOrders(tickerWithOrders)
//                       .side(Side.SELL)
//                       .build()).toList();

        var sortByBidPriceAscending = sortByBidPriceAscending(orderBooks);

        ordersGroupedBySide.put(Side.SELL, sortByAskPriceDescending);
        ordersGroupedBySide.put(Side.BUY, sortByBidPriceAscending);

        return ordersGroupedBySide;
    }

    private List<OrderProcessObject> transformIntoOrderProcess( Map<String, List<OrderBookDto>> sortedOrderBooks, Side side) {
       return sortedOrderBooks.entrySet().stream()
                .map(x -> TickerWithOrders.builder()
                        .ticker(x.getKey())
                        .orders(x.getValue())
                        .build())
                .map(tickerWithOrders -> OrderProcessObject.builder()
                        .tickerWithOrders(tickerWithOrders)
                        .side(side)
                        .build()).toList();
    }
    private Map<String, List<OrderBookDto>> sortByAskPriceDescending(List<OrderBookDto> orderBooks) {

        /***
         * Sort all orders belonging to a TICKER by ask price desc
         * i.e A seller would want to see the highest ask price  first
         ***/
        final ObjectMapper mapper = new ObjectMapper(); //

        var orderBooksBy = orderBooks.stream()
                .collect(Collectors.groupingBy(OrderBookDto::getTicker, Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                                .sorted(Comparator.comparingDouble(OrderBookDto::getAskPrice).reversed())
                                .toList()
                )));


      return   orderBooks.stream()
                .collect(Collectors.groupingBy(OrderBookDto::getTicker, Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                                .sorted(Comparator.comparingDouble(OrderBookDto::getAskPrice).reversed())
                                .toList()
                )));
    }

    private Map<String, List<OrderBookDto>> sortByBidPriceAscending(List<OrderBookDto> orderBooks) {
        /***
         * Sort all orders belonging to a TICKER by bid price asc
         * i.e A buyer would want to see the lowest bid price  first
         ***/
       return orderBooks.stream()
                .collect(Collectors.groupingBy(OrderBookDto::getTicker, Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                                .sorted(Comparator.comparingDouble(OrderBookDto::getBidPrice))
                                .toList()
                )));
    }


    @Override
    public void receiveOrderBooks(List<OrderBookDto> orderBooks, Exchange type) {
        emitterService.initPublishEvent(orderBooks);
        var ordersToPublish =  transformData(orderBooks);
        sendOrderBooksToPublisher(ordersToPublish, type);

    }

    private void sendOrderBooksToPublisher(Map<Side, Map<String, List<OrderBookDto>>> orders, Exchange exchange) {
       Map<Exchange, Map<Side, Map<String, List<OrderBookDto>>>> as = new HashMap<>();
       as.put(exchange, orders);
       redisMessagePublisher.publish2(as);
       /* var ordersFromExchange = OrderFromExchange.builder()
                        .orders(orders)
                        .exchange(exchange)
                                .build();
       redisMessagePublisher.publish(ordersFromExchange);*/
    }
}
