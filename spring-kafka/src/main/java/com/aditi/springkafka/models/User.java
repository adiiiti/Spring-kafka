package com.aditi.springkafka.models;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Date;

@Component
public class User {
    private String topic;
    private Date startTime;
    private Date endTime;

    public User(String topic, Date startTime, Date endTime) {
        this.topic = topic;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public User() {
    }

    public String getTopic() {
        return topic;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
