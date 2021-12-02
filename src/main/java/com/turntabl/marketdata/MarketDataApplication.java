package com.turntabl.marketdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class MarketDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketDataApplication.class, args);
//        ProcessBuilder pb = new ProcessBuilder(args);
//        Map<String, String> env = pb.environment();
//        env.put("REDIS_HOST", "local");
//        try {
//            Process process = pb.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.getenv().forEach((x , b)-> System.out.println(x + "=========="+ b));
//        System.getProperties().forEach((c,v) -> System.out.println(c + "======" + v));
        var host = System.getenv("TEST");
//        int port= Integer.parseInt(System.getenv("REDIS_PORT"));
        String password = System.getenv("REDIS_PASSWORD");
        log.info("Rei details {} in main===", host);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
