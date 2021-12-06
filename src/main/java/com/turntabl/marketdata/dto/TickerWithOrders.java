package com.turntabl.marketdata.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = TickerWithOrders.TickerWithOrdersBuilder.class)
public class TickerWithOrders {
    private String ticker;
    private List<OrderBookDto> orders;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TickerWithOrdersBuilder {}
}
