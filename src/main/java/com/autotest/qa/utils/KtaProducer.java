package com.autotest.qa.utils;


import com.autotest.qa.cases.KtaMainFlowTest;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KtaProducer implements Runnable {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public KtaProducer(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.12.116:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        this.producer = new KafkaProducer<String, String>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            producer.send(new ProducerRecord<String, String>(topic,"message",KtaMainFlowTest.register()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    public static void main(String[] args) {
        KtaProducer ktaProducer=new KtaProducer("test");
        Thread thread=new Thread(ktaProducer);
        thread.start();
    }

}

