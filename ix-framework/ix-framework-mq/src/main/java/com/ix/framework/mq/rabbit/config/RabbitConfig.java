package com.ix.framework.mq.rabbit.config;

import com.ix.framework.mq.rabbit.dynamic.RabbitModuleInitializer;
import com.ix.framework.mq.rabbit.dynamic.RabbitModuleProperties;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: rabbit配置
 */
@AutoConfiguration
@EnableConfigurationProperties(RabbitModuleProperties.class)
public class RabbitConfig {
    /**
     * 消息序列化配置
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    /**
     * 动态创建队列、交换机初始化器
     */
    @Bean
    @ConditionalOnMissingBean
    public RabbitModuleInitializer rabbitModuleInitializer(AmqpAdmin amqpAdmin,
                                                           RabbitModuleProperties rabbitModuleProperties) {
        return new RabbitModuleInitializer(amqpAdmin, rabbitModuleProperties);
    }
}
