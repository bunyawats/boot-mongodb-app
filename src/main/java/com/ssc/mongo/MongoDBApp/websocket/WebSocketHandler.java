package com.ssc.mongo.MongoDBApp.websocket;

import com.ssc.mongo.MongoDBApp.service.LoanProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ApplicationScope
public class WebSocketHandler extends TextWebSocketHandler {

    public static String SESSION_KEY = "sessionKey";

    private Map<String, WebSocketSession> ssKey2wsSsMap = new ConcurrentHashMap<>();

    @Autowired
    LoanProcessService loanProcessService;

    @Override
    public void handleTextMessage(WebSocketSession wsSession, TextMessage message) {

        log.info("handleTextMessage {}", message);
        String patLoad = message.getPayload();
        if (patLoad.contains(SESSION_KEY)) {
            String pcInstID = patLoad.substring( SESSION_KEY.length()+1);
            log.info(SESSION_KEY + " : {}",  pcInstID);
            ssKey2wsSsMap.put(pcInstID, wsSession);

            loanProcessService.completeCurrentActivity(pcInstID);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
        super.afterConnectionEstablished(wsSession);

        log.info("afterConnectionEstablished {}", wsSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) throws Exception {
        super.afterConnectionClosed(wsSession, status);

        log.info("afterConnectionClosed {}", wsSession.getId());
        ssKey2wsSsMap.values().remove(wsSession);
    }


    public void sendMessageBySessionKey(String sessionKey, String payload) {

        WebSocketSession wsSession = ssKey2wsSsMap.get(sessionKey);
        log.info("sessionKey : {} -> wsSession : {} \n\n ", sessionKey, wsSession);

        if (wsSession != null) {
            log.info("wsSession is open : {}  ", wsSession.isOpen());
            if (wsSession.isOpen()) {
                try {
                    TextMessage resMessage = new TextMessage(payload);
                    wsSession.sendMessage(resMessage);
                } catch (IOException e) {
                    log.info(" WebSocket Exception on send {} ", e.getMessage());
                }
            } else {
                ssKey2wsSsMap.values().remove(wsSession);
            }
        }
    }
}