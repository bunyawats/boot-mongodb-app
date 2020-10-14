package com.ssc.mongo.MongoDBApp.service;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LoanProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;


    public String createLoanProcess(String processId) {

        log.info("---call createLoanProcess: {}", processId);

        String processInstanceId = runtimeService.startProcessInstanceByKey(processId)
                .getProcessInstanceId();
        log.info("---started instance: {}", processInstanceId);

        return processInstanceId;
    }

    public Map<String, String> getLoanState(String processInstanceId) {

        log.info("call getLoanState: {}", processInstanceId);

        Map message = new HashMap<String, String>();

        message.put("processInstanceId", processInstanceId);

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (task != null) {
            log.info("task id: {}", task.getId());
            log.info("task TaskDefinitionKey: {}", task.getTaskDefinitionKey());

            message.put("taskId", task.getId());
            message.put("taskDefinitionKey", task.getTaskDefinitionKey());
        }

        return message;
    }

    public void completeCurrentActivity(String processInstanceId) {{

        log.info("call completeCurrentActivity: {}", processInstanceId);

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            log.info("task id: {}", task.getId());

            taskService.complete(task.getId());
        }
    }}

    public Map<String, String> completeLoanTask(String taskId) {

        log.info("call completeLoanTask: {}", taskId);

        Map message = new HashMap<String, String>();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task != null) {
            taskService.complete(task.getId());
            log.info("task id: {}", task.getId());
            log.info("task TaskDefinitionKey: {}", task.getTaskDefinitionKey());

            message.put("taskId", task.getId());
            message.put("taskDefinitionKey", task.getTaskDefinitionKey());


        } else {
            message.put("message", "task not found");
        }

        return message;
    }

    public Map<String, String> submitLoanTaskForm(String taskId, Map formVariableMap) {

        log.info("call submitLoanTaskForm: {}", taskId);

        Map message = new HashMap<String, String>();

        try {
            formService.submitTaskForm(taskId, formVariableMap);
            //          Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

            message.put("message", e.getMessage());
        }

        return message;
    }


    public Map<String, String> getTaskVariable(String taskId) {

        log.info("call getTaskVariable: {}", taskId);

        Map message = new HashMap<String, String>();

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task != null) {

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> formFields = taskFormData.getFormFields();
            String taskFormKey = taskFormData.getFormKey();

            log.info("formFields{}", formFields);
            log.info("taskFormKey {}", taskFormKey);

            message.put("taskId", task.getId());
            message.put("formFields", formFields);
            message.put("taskFormKey", taskFormKey);

        } else {
            message.put("message", "task not found");
        }

        return message;
    }


}
