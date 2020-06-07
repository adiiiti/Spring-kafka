package com.aditi.springkafka.models;

import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalTime;
import java.util.Date;

//create A JSON object to be returned in the form of (message, UTC time of creation)
@Component
public class messageObject {
    private String message;
    private Date time;
    private int partition;

    public messageObject(String message, Date time, int partition) {
        this.message = message;
        this.time=time;
        this.partition=partition;
    }

    public messageObject() {
    }

    public String getMessage() {
        return message;
    }

    public int getPartition() {
        return partition;
    }

    public Date getTime() {
        return time;
    }

}
