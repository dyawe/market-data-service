package com.turntabl.marketdata.resources;

import com.turntabl.marketdata.service.serviceImpl.EmitterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@CrossOrigin(value = "http://localhost:5500", maxAge = Long.MAX_VALUE)
@RequestMapping(value = "mq/", consumes = MediaType.ALL_VALUE)
public class EmitterResource {

    @Autowired
    private EmitterServiceImpl emitterService;

    @GetMapping("market-data")
    public SseEmitter subscribeToSSE() {
        return emitterService.getSSEInstance();
    }
}
