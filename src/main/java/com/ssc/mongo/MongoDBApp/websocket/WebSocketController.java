package com.ssc.mongo.MongoDBApp.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WebSocketController {

    @Autowired
    WebSocketHandler wstHandler;

    @GetMapping("notifyWS/{sessionKey}")
    public String notifyWebSocketBySessionKey(@PathVariable("sessionKey") String sessionKey) {

        String wsMessage = "Message from WebSocketController";
        wstHandler.sendMessageBySessionKey(
                sessionKey,
                wsMessage);

        return wsMessage;
    }

}
