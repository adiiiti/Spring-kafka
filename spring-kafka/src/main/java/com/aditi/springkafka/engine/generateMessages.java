package com.aditi.springkafka.engine;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;


//NO USE CLASS
@Service
public class generateMessages {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "users";
    int recordCount=0;

    public void getMessages() {
        System.out.println("Hey");

//        ArrayList<String> messagesFromKafka = new ArrayList<>();
//
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        //props.put("max.partition.fetch.bytes", "1048576000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//
//
          consumer.subscribe(Arrays.asList(TOPIC));
        ConsumerRecords<String, String> records = consumer.poll(1000);
        recordCount = records.count();
        logger.info("recordCount " + recordCount);

//          List<PartitionInfo> partitions = kafkaConsumer.partitionsFor(TOPIC);
//          System.out.println(partitions);
//        TopicPartition topicPartition = new TopicPartition(TOPIC, 0);
//        logger.info("Subscribed to topic " + kafkaConsumer.listTopics());
//        // will consume all the messages and store in records
//        try{
//            while(true){
//                ConsumerRecords<String, String> records = consumer.poll(1000);
//                recordCount = records.count();
//                logger.info("recordCount " + recordCount);
//            }
//        }
//        finally {
//            consumer.close();
//        }

//        kafkaConsumer.seekToBeginning(topicPartition);
//
//        // getting total records count

//        for(ConsumerRecord<String, String> record : records) {
//           if(record.value() != null) {
//              logger.info("kafkaMessage "+record.value());
//              messagesFromKafka.add(record.value());
//           }
//        }
//        kafkaConsumer.close();
//        return messagesFromKafka;
    }
}
