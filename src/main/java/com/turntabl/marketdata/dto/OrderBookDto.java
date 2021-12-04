package com.turntabl.marketdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderBookDto {
    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("SELL_LIMIT")
    private int sellLimit;

    @JsonProperty("BUY_LIMIT")
    private int buyLimit;

    @JsonProperty("MAX_PRICE_SHIFT")
    private float maxPriceShift;

    /**
     * @apiNote Highest Price(eg 30GHS) at which a Buyer will Buy the stock i.e
     *  Either i buy at 30GHS Last or lower. Not enough to buy above 30
     */
    @JsonProperty("BID_PRICE")
    private float bidPrice;

    /**
     * @apiNote Lowest Price (eg 50GHS) at which a seller will sell the stock. i.e
     * either I sell at 50GHS or Higher. Not Lower ever
     */
    @JsonProperty("ASK_PRICE")
    private float askPrice;

    @JsonProperty("LAST_TRADED_PRICE")
    private float lastTradedPrice;

    public OrderBookDto() {
    }

    public OrderBookDto(String ticker, int sellLimit, int buyLimit, float maxPriceShift) {
        this.ticker = ticker;
        this.sellLimit = sellLimit;
        this.buyLimit = buyLimit;
        this.maxPriceShift = maxPriceShift;
    }
}
