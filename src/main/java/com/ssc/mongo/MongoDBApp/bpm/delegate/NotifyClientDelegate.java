package com.ssc.mongo.MongoDBApp.bpm.delegate;


import com.ssc.mongo.MongoDBApp.websocket.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifyClientDelegate implements JavaDelegate {

    @Autowired
    WebSocketHandler wstHandler;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("executed notifyClientDelegate: {}", execution);
        log.info("executed getVariableNames: {}", execution.getVariableNames());
        log.info("executed getActivityInstanceId: {}", execution.getActivityInstanceId());
        log.info("executed getProcessInstanceId: {}", execution.getProcessInstanceId());
        log.info("executed getVariables: {}", execution.getVariables());

        wstHandler.sendMessageBySessionKey(
                execution.getProcessInstanceId(),
                "Message from NotifyClientDelegate");
    }

}
