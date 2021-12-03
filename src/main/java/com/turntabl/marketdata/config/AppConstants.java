package com.turntabl.marketdata.config;

import com.turntabl.marketdata.enums.Exchange;
import lombok.Data;
import lombok.Getter;

@Getter
public  class AppConstants {
    private static final String exchangeOne = Exchange.ONE.name();
    private static final String exchangeTwo = Exchange.TWO.name();


}
