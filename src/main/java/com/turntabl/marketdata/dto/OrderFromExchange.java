package com.turntabl.marketdata.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.turntabl.marketdata.enums.Exchange;
import com.turntabl.marketdata.enums.Side;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = OrderFromExchange.OrderFromExchangeBuilder.class)
public class OrderFromExchange {
    private Exchange exchange;
    private Map<Side, Map<String, List<OrderBookDto>>> orders;


    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderFromExchangeBuilder {}
}
