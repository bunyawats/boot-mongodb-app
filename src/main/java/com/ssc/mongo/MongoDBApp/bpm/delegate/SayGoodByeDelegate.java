package com.ssc.mongo.MongoDBApp.bpm.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SayGoodByeDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("executed sayGoodByeDelegate: {}", execution);
        log.info("executed getVariableNames: {}", execution.getVariableNames());
        log.info("executed getActivityInstanceId: {}", execution.getActivityInstanceId());
        log.info("executed getVariables: {}", execution.getVariables());
    }

}
