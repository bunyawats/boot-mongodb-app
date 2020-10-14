package com.ssc.mongo.MongoDBApp.sse;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;


@RestController
@Slf4j
public class SseController {

    @Autowired
    SseEmitters sseEmitters;


    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping(path = "sse/{sessionKey}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter createConnection(@PathVariable("sessionKey") String sessionKey) {

        log.info("create sse with  sessionKey : {}", sessionKey);

        return sseEmitters.add(sessionKey, new SseEmitter());
    }


    @GetMapping("notifysse/{sessionKey}")
    public SSEMessage notifySSE(@PathVariable("sessionKey") String sessionKey) {

        SSEMessage message = new SSEMessage("sse message", 55L, new Date());

        log.info("notify sse message : {} ", message);

        sseEmitters.send(sessionKey, message);

        return message;
    }

}
