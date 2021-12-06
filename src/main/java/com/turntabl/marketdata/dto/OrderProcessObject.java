package com.turntabl.marketdata.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.turntabl.marketdata.enums.Side;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = OrderProcessObject.OrderProcessObjectBuilder.class)
public class OrderProcessObject {
    private Side side;
    private TickerWithOrders tickerWithOrders;


    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderProcessObjectBuilder {}
}
