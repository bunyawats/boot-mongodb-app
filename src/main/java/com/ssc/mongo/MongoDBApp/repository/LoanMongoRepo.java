package com.ssc.mongo.MongoDBApp.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class LoanMongoRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Document getLoanConfigByAppVersion(String appVersion) {

        Document document = null;
        try {

            MongoCollection<Document> collection =
                    mongoTemplate.getCollection("LoanAppFormConfiguration");

            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("loan_application_version", appVersion);
            MongoIterable<Document> cursor = collection.find(searchQuery);

            document = cursor.first();


        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return document;
    }

    public void upsertLoanAppDoc(Document submittedDocument) {

        MongoCollection<Document> collection =
                mongoTemplate.getCollection("LoanApplications");

        String submittedApplicationId = submittedDocument.getString("loan_application_id");
        log.info("submittedApplicationId : {} ", submittedApplicationId);

        if (submittedApplicationId != null) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("loan_application_id", submittedApplicationId);

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.putAll(submittedDocument);

            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);

            UpdateResult updateResult = collection.updateOne(searchQuery, updateObject);
            log.info("{}", updateResult.getMatchedCount());
            if (updateResult.getMatchedCount() == 0) {

                collection.insertOne(submittedDocument);
            }
        }
    }
}
