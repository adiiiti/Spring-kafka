package com.aditi.springkafka.models;

public class ProducerObject {
    String topic;
    String message;

    public ProducerObject(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
