package com.aditi.springkafka.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;


@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private long curtimestamp;


    public void sendMessage(String topic, String message) throws ExecutionException, InterruptedException {

        ListenableFuture<SendResult<String, String>> future= this.kafkaTemplate.send(topic, message);

        try {
            future.get();
        } catch(Throwable ex) {
            logger.error("Message Post Failed. '{}'", ex.getCause().getMessage(), ex);
            throw ex;
        }

        SendResult<String, String> a = future.get();
        this.curtimestamp=a.getRecordMetadata().timestamp();

        String newtimestamp=Instant.ofEpochMilli(curtimestamp).atOffset(ZoneOffset.UTC).toString();
        String time=newtimestamp.substring(11,16);


        logger.info(String.format("#### -> Producing message -> %s", message+" "+time));
    }

}
