package com.ssc.mongo.MongoDBApp.controller;

import com.ssc.mongo.MongoDBApp.repository.LoanMongoRepo;
import com.ssc.mongo.MongoDBApp.service.LoanProcessService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class LoanApplicationController {


    @Autowired
    private LoanMongoRepo loanMongoRepo;

    @Autowired
    private LoanProcessService loanProcessService;


    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping(
            value = "loanapp",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity createLoanApplication(HttpEntity<String> httpEntity) {

        String json = httpEntity.getBody();

        ResponseEntity<String> responseEntity = null;

        try {
            Document submittedDocument = Document.parse(json);
            log.info(submittedDocument.toJson());

            loanMongoRepo.upsertLoanAppDoc(submittedDocument);

            String submittedTaskId = submittedDocument.getString("taskId");
            log.info("submittedTaskId : {} ", submittedTaskId);
            if (submittedTaskId != null) {

                Map formVariableMap = new HashMap();
                submittedDocument.entrySet().stream().forEach(
                        elem -> formVariableMap.put(
                                elem.getKey(),
                                elem.getValue()));

                loanProcessService.submitLoanTaskForm(submittedTaskId, formVariableMap);
            }

            responseEntity = new ResponseEntity(json, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

            Map message = new HashMap<String, String>();
            message.put("message", "invalid json format");

            responseEntity = new ResponseEntity(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}
