package com.ssc.mongo.MongoDBApp.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@ApplicationScope
@Component
@Slf4j
public class SseEmitters {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    SseEmitter add(String name, SseEmitter emitter) {

        log.info(" new emitter : {}", emitter);

        this.emitterMap.put(name, emitter);

        log.info(" emitterMap size = {} ", this.emitterMap.size());

        emitter.onCompletion(() -> {
            this.emitterMap.values().remove(emitter);
            log.info(" emitterMap size = {} ", this.emitterMap.size());
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitterMap.values().remove(emitter);
            log.info(" emitterMap size = {} ", this.emitterMap.size());
        });

        return emitter;
    }

    void send(String name, Object obj) {
        List<SseEmitter> failedEmitters = new ArrayList<>();

        this.emitterMap.forEach((key, emitter) -> {
            try {
                if (key.equalsIgnoreCase(name)) {
                    log.info("send message : {} to {}", obj, key);
                    emitter.send(obj);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
                failedEmitters.add(emitter);
            }
        });

        failedEmitters.forEach(failedEmitter -> {
            log.info(" remove failed emitter : {} from map", failedEmitter);
            this.emitterMap.values().remove(failedEmitter);
            log.info(" emitterMap size = {} ", this.emitterMap.size());
        });

    }
}
