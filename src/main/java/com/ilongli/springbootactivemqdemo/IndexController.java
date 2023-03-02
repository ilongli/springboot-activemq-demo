package com.ilongli.springbootactivemqdemo;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author ilongli
 * @date 2023/3/2 9:11
 */
@RestController
public class IndexController {

    @Resource(name = "testQueue")
    private Queue testQueue;

    @Resource(name = "testTopic")
    private Topic testTopic;

    @Resource(name = "ackQueue")
    private Queue ackQueue;


    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("")
    public String index() {
        return "springboot-activemq-demo";
    }


    @GetMapping("test1")
    public String test1(String msg) {
        jmsMessagingTemplate.convertAndSend(testQueue, msg);
        return "[test1]ok";
    }


    @GetMapping("test2")
    public String test2(String msg) {
        jmsMessagingTemplate.convertAndSend(testTopic, msg);
        return "[test2]ok";
    }


    @GetMapping("test3")
    public String test3(String msg) {
        jmsMessagingTemplate.convertAndSend(ackQueue, msg);
        return "[test3]ok";
    }

}

