package com.turntabl.marketdata.config;

import com.turntabl.marketdata.exceptions.AlreadySubscribedException;
import com.turntabl.marketdata.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class InitConfig {
    private final MarketDataService marketDataService;

    /**
     * Runs when the application starts and attempts subscribing to the exchange data
     * @exception  AlreadySubscribedException when subscription has already occurred
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initSubscriptions() {
        log.info("Initializing app! please wait.......");
        try {
            marketDataService.subscribe();
        } catch (AlreadySubscribedException exception) {
           log.info("AlreadySubscribedException occurred while subscribing to exchange");
        }
    }
}
