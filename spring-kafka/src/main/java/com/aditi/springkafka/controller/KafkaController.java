package com.aditi.springkafka.controller;

import com.aditi.springkafka.engine.*;
import com.aditi.springkafka.models.ProducerObject;
import com.aditi.springkafka.models.User;
import com.aditi.springkafka.models.User2;
import com.aditi.springkafka.models.messageObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.Api;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//This is the controller class
@RestController
@RequestMapping(value="/kafka")
@Api(value="kafka end point", description = "deals with the Kafka messages")
public class KafkaController {
    private final Producer producer;
    private ProducerObject producerObject;
    private fetchMessages fetch_messages;
    private fetchmessages_2 fetch_messages_2;

    @Autowired
    KafkaController(Producer producer, fetchMessages fetch_messages, fetchmessages_2 fetch_messages_2){
        this.producer = producer;
        this.fetch_messages = fetch_messages;
        this.fetch_messages_2 = fetch_messages_2;
    }

    @PostMapping("/publish")
    //This is the request to produce and add any messages to the topic entered by user
    public String sendMessageToKafkaTopic (@RequestBody ProducerObject producerObject) throws Throwable {
        String topic, message;
        topic=producerObject.getTopic();
        message=producerObject.getMessage();
        this.producer.sendMessage(topic, message);
        return "Successful";
    }

    @PostMapping("/getMessages")
    //This is the request to fetch the messages from the topic, within startTime-endTime time range
    //This is the improved function using "offetsforTimestamps" method
    public ArrayList<messageObject> publishMessages (@RequestBody User user) {
        String topic=user.getTopic();
        Date startTime=user.getStartTime();
        Date endTime=user.getEndTime();
        ArrayList<messageObject> messages = fetch_messages.showMessages(topic,startTime,endTime);
        return messages;
        //return "Successful2";
    }


    @PostMapping("/getMessages_2")
    //This is the again request to fetch the messages from the topic, within startTime-endTime time range.
    //But this uses a function which fetches all the records from topic(Higher complexity)
    public String publishMessages_2 (@RequestBody User user) {
        String topic=user.getTopic();
        Date startTime=user.getStartTime();
        Date endTime=user.getEndTime();
        fetch_messages_2.showMessages(topic,startTime,endTime);
        return "Successful2";
    }


}
