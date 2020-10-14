package com.ssc.mongo.MongoDBApp.controller;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssc.mongo.MongoDBApp.service.LoanProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class LoanProcessController {


    @Autowired
    private LoanProcessService loanProcessService;


    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping(
            value = "loanprocess",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity createLoanProcess(HttpEntity<String> httpEntity) {


        ResponseEntity<String> responseEntity = new ResponseEntity(
                "Can not handle error.",
                HttpStatus.INTERNAL_SERVER_ERROR);


        Map message = new HashMap<String, String>();

        try {

            String json = httpEntity.getBody();
            log.info("request body {}", json);

            JsonElement jsonElement = JsonParser.parseString(json);
            log.info("request jsonElement {}", json);
            String processId = jsonElement.getAsJsonObject()
                    .get("processId").getAsString();

            String processInstanceId = loanProcessService.createLoanProcess(processId);

            if (processInstanceId != null) {

                message.put("processInstanceId", processInstanceId);

                responseEntity = new ResponseEntity(
                        message,
                        HttpStatus.CREATED);
            }

        } catch (Throwable e) {

            log.info("\n\n\n\n We can catch this error");
            log.info(e.getMessage());

            message.put("message", e.getMessage());

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }


    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping(
            value = "currentloanstate",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity getLoanState(HttpEntity<String> httpEntity) {

        ResponseEntity<String> responseEntity = null;
        Map message = new HashMap<String, String>();

        try {

            String json = httpEntity.getBody();
            log.info("request body {}", json);

            JsonElement jsonElement = JsonParser.parseString(json);
            log.info("request jsonElement {}", json);
            String processInstanceId = jsonElement.getAsJsonObject()
                    .get("processInstanceId").getAsString();


            message = loanProcessService.getLoanState(processInstanceId);

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

            message.put("message", e.getMessage());

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping(
            value = "loanFormVariable",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity getLoanFormVariable(HttpEntity<String> httpEntity) {

        ResponseEntity<String> responseEntity = null;
        Map message = new HashMap<String, String>();

        try {

            String json = httpEntity.getBody();
            log.info("request body {}", json);

            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String taskId = jsonObject.get("taskId").getAsString();

            message = loanProcessService.getTaskVariable(taskId);

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

            message.put("message", e.getMessage());

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping(
            value = "completeloanTask",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity completeLoanTask(HttpEntity<String> httpEntity) {

        ResponseEntity<String> responseEntity = null;
        Map message = new HashMap<String, String>();

        try {

            String json = httpEntity.getBody();
            log.info("request body {}", json);

            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String taskId = jsonObject.get("taskId").getAsString();

            message = loanProcessService.completeLoanTask(taskId);

            if (message.get("taskId") != null) {

                responseEntity = new ResponseEntity(
                        message,
                        HttpStatus.CREATED);
            } else {

                responseEntity = new ResponseEntity(
                        message,
                        HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

            message.put("message", e.getMessage());

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}
