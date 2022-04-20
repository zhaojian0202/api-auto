package com.autotest.qa.testNG.listener;

import com.autotest.qa.utils.PropertiesUtil;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class TestNgListener implements ISuiteListener,IHookable {

    private static final Logger logger= LoggerFactory.getLogger(TestNgListener.class);
    @Override
    public void onStart(ISuite suite) {
        System.out.println("开始执行测试套件:"+suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("结束测试");
        destroy();
    }

    @Override
    public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {

    }

    public Producer<String, String> producer(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertiesUtil.getValue("kafka.server.domain"));
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.ACKS_CONFIG,"0");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;
    }

    /**
     * 测试完成的后置监听器:
     * 1.停止相关的测试进程
     * 2.收集测试日志发送到平台
     * 3.生成本次测试的报告
     */
    public void destroy(){
        logger.debug("testingFinishListener stop ....");
                if ("true".equals(PropertiesUtil.getValue("log.push"))){
            String topic = PropertiesUtil.getValue("kafka.log.topic");
            Future<RecordMetadata> result = producer().send(new ProducerRecord<String, String>(topic,"k","v"));
            try {
                RecordMetadata metadata = result.get();
            }catch (Exception e){
                e.printStackTrace();
            }
            logger.info("日志发送Kafka成功，BuildId==>{}","key");
//            new RestTemplate().postForEntity(logServerDomain + LOG_PUSH_PATH,RunObserver.getTestingLogDTO(),null);
        }
        logger.info("运行的日志为:\n"+"v");
    }


}
