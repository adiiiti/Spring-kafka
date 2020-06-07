package com.aditi.springkafka.engine;

import com.aditi.springkafka.models.messageObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.scenario.effect.Offset;
import org.apache.catalina.core.ApplicationContext;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;


@Component
public class fetchMessages{
    private ArrayList<messageObject> messages;
    private String topic;
    private KafkaConsumer<String, String> consumer;

    //This function converts the timestamp into UTC time
    private String getTime(long timestamp) {
        String newtimestamp = Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).toString();
        newtimestamp = newtimestamp.substring(11, 19);
        return newtimestamp;
    }

    private Date getDate(long timestamp) {
        Date date = new Date(timestamp);
        return date;
    }

    private void addToList(ConsumerRecord<String, String> record) {
        messageObject message_Object = new messageObject(record.value(), getDate(record.timestamp()), record.partition());
        System.out.println(record.value());
        messages.add(message_Object);
    }

    private void getMessagesForPartition(TopicPartition partition, long seekOffset, long endTimestamp){

        try {
            ConsumerRecords<String, String> records;
            consumer.assign(Arrays.asList(partition));
            consumer.seek(partition, seekOffset);
            while (true) {
                records = consumer.poll(Duration.ofSeconds(1L));
                if(records.count()==0) break;
                stream(records.spliterator(), false)
                        .filter(record -> record.timestamp() <= endTimestamp)
                        .forEach(record -> {
                            addToList(record);
                        });

                Optional<ConsumerRecord<String, String>> answer = stream(records.spliterator(), false)
                        .filter(record -> record.timestamp() > endTimestamp)
                        .findFirst();
                if (answer.isPresent()) break;
            }
        }
        catch(NullPointerException ex){
            System.out.println("No consumption for this partition");
        }

    }

    //The main function to fetch all the messages, from all the partitions
    public ArrayList<messageObject> showMessages(String topic, Date startTime, Date endTime) {

        this.topic = topic;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "3");

        //creating a consumer
        this.consumer = new KafkaConsumer<String, String>(props);
        this.messages = new ArrayList<>();

        long startTimestamp = startTime.getTime(), endTimestamp = endTime.getTime();
        //System.out.println(startTimestamp+" "+endTimestamp);

        //subscribing to the topic
        consumer.subscribe(Arrays.asList(topic));

        Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
        consumer.partitionsFor(topic)
                 .forEach(partitionInfo -> timestampsToSearch.put(new TopicPartition(topic, partitionInfo.partition()), startTimestamp));
        //System.out.print(timestampsToSearch.size());
        Map<TopicPartition, OffsetAndTimestamp> outOffsets = consumer.offsetsForTimes(timestampsToSearch);
//        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : outOffsets.entrySet()) {
//            System.out.println(entry.getKey().partition() + ":" + entry.getValue().offset());
//        }
        consumer.unsubscribe();
        outOffsets.entrySet().stream()
                .filter(e -> e.getValue()!=null)
                .forEach(e -> {getMessagesForPartition(e.getKey(), e.getValue().offset(), endTimestamp);});



        consumer.close();

        return this.messages;

    }

}
