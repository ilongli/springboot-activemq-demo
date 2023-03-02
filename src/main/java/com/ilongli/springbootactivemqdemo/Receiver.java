package com.ilongli.springbootactivemqdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author ilongli
 * @date 2023/3/2 9:35
 */
@Component
@Slf4j
public class Receiver {

    @JmsListener(destination = "test-queue", containerFactory = "queueListenerFactory")
    public void testQueueHandler(String msg) {
        log.info("接收到test-queue的消息: {}", msg);
        // 故意报错
        int i = 1 / 0;
    }

    @JmsListener(destination = "test-topic", containerFactory = "topicListenerFactory")
    public void testTopicHandler1(String msg) {
        log.info("[1]接收到test-topic的消息: {}", msg);
    }

    @JmsListener(destination = "test-topic", containerFactory = "topicListenerFactory")
    public void testTopicHandler2(String msg) {
        log.info("[2]接收到test-topic的消息: {}", msg);
        // 故意报错
        int i = 1 / 0;
    }


    @JmsListener(destination = "ack-queue", containerFactory = "ackListenerFactory")
    public void testAckHandler(ActiveMQMessage message, Session session) throws JMSException {

        log.info("接收到ack-topic的消息: {}", message);

        if (message.getJMSRedelivered()) {
            int count = message.getIntProperty("JMSXDeliveryCount");
            log.info("重试:{}", count);
            if (count == 2) {
                message.acknowledge();
                return;
            }
        }

        session.recover();
    }

}
