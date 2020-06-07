package com.aditi.springkafka.engine;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Component
public class fetchmessages_2 {

           private static String getTime(long curtimestamp){
           String newtimestamp= Instant.ofEpochMilli(curtimestamp).atOffset(ZoneOffset.UTC).toString();
           //String time=newtimestamp.substring(11,16);
           return newtimestamp;
       }

       private static Boolean getTime(String time, String startTime, String endTime){

           //System.out.println(time);
           LocalTime target = LocalTime.parse(time) ;
           Boolean targetInRange = target.isAfter( LocalTime.parse(startTime))
                                  && target.isBefore( LocalTime.parse( endTime ) ) ;
           return targetInRange;
       }

       public void showMessages(String topic, Date startTime, Date endTime) {
           Properties props = new Properties();

           props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
           props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
           props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
           props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
           props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");


           KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
           ArrayList<ConsumerRecord<String, String>> messages_fetched = new ArrayList<ConsumerRecord<String, String>>();

           consumer.subscribe(Arrays.asList(topic));
           ConsumerRecords<String, String> records = consumer.poll(0);
           consumer.seekToBeginning(consumer.assignment());

           //System.out.println(records.count());
           int recordCount=5;

           try {
               while (recordCount != 0) {
                   records = consumer.poll(1000);
                   recordCount = records.count();
                   System.out.println(recordCount);
                   for (ConsumerRecord<String, String> record : records) {
                       long curtimestamp = record.timestamp();
                       String time = getTime(curtimestamp);
                       //Boolean targetInRange = getTime(time, startTime, endTime);
//                       if (targetInRange) {
//                           messages_fetched.add(record);
//                           System.out.println(time + " " + curtimestamp);
//                       }
                       messages_fetched.add(record);
                     System.out.println(record.partition() + " " + record.value()+" "+record.offset()+" "+record.timestamp());

                   }
               }

           } finally {
               consumer.close();
           }
       }
}
