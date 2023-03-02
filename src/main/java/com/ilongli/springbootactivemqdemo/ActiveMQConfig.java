package com.ilongli.springbootactivemqdemo;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author ilongli
 * @date 2023/3/2 9:26
 */
@Configuration
public class ActiveMQConfig {


    @Bean
    public ActiveMQConnectionFactoryCustomizer configureActiveMQConnectionFactory() {
        return connectionFactory ->
        {
            // 设置重发策略
            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setMaximumRedeliveries(3);
            redeliveryPolicy.setInitialRedeliveryDelay(2000L);
            redeliveryPolicy.setRedeliveryDelay(2000L);
            redeliveryPolicy.setUseExponentialBackOff(true);
            redeliveryPolicy.setBackOffMultiplier(2);
            connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        };
    }

/*    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        return jmsTemplate;
    }*/

    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // 将spring当前默认的配置应用到新的factory里面
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(false);
//        factory.setConnectionFactory(connectionFactory);
//        factory.setSessionTransacted(true);
        return factory;
    }


    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // 将spring当前默认的配置应用到新的factory里面
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
//        factory.setConnectionFactory(connectionFactory);
//        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> ackListenerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // 将spring当前默认的配置应用到新的factory里面
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(false);
        factory.setSessionAcknowledgeMode(4);
//        factory.setConnectionFactory(connectionFactory);
//        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean("testQueue")
    public Queue testQueue() {
        return new ActiveMQQueue("test-queue");
    }

    @Bean("testTopic")
    public Topic testTopic() {
        return new ActiveMQTopic("test-topic");
    }

    @Bean("ackQueue")
    public Queue ackQueue() {
        return new ActiveMQQueue("ack-queue");
    }

}
