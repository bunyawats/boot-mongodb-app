package com.ssc.mongo.MongoDBApp.sse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SSEMessage {

    private String name;

    private Long amount;

    private Date date;

}
