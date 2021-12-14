package com.turntabl.marketdata.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmitterServiceImpl {

    List<SseEmitter> activeEmittersList = new CopyOnWriteArrayList<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public SseEmitter getSSEInstance() {
        try {
            SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
            sseEmitter.send(SseEmitter.event().name("market-data"));

            sseEmitter.onCompletion(()-> {
                log.info("SUCCESS => event published");
                activeEmittersList.remove(sseEmitter);
            });

            sseEmitter.onError(sseInstance-> {
                log.info("FAILURE => event publishing failed");
                activeEmittersList.remove(sseEmitter);
            });

            sseEmitter.onTimeout(() -> {
                log.info("FAILURE => sseEmitter timed out");
                activeEmittersList.remove(sseEmitter);
            });

            activeEmittersList.add(sseEmitter);

//            initPublishEvent(sseEmitter,"Report report");
            return sseEmitter;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No emitter instance was found!");
        }
    }

    public <T>  void initPublishEvent(SseEmitter sseEmitter, T report) {
        SseEmitter.SseEventBuilder sseEvent = sseEmitter.event().name("market-data").data(report);;
        publishEvent(sseEvent);
    }

    public <T>  void initPublishEvent(T report) {
        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event().name("market-data").data(report);;
        publishEvent(sseEvent);
    }

    private void publishEvent(SseEmitter.SseEventBuilder sseEvent) {
        executor.execute(()-> activeEmittersList.forEach(sseEmitter -> {
            try {
                sseEmitter.send(sseEvent);
                log.info("SUCCESS => event published");
            } catch (IOException e) {
                log.info("FAILURE => sseEmitter timed out with cause {}", e.getMessage());
            }
        }));
    }
}
