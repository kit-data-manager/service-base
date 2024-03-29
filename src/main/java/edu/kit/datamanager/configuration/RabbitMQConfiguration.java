/*
 * Copyright 2018 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.configuration;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Properties for RabbitMQ inside SpringBoot applications.
 * 
 * @author jejkal
 */
@Configuration
@Data
@ConditionalOnExpression("${repo.messaging.enabled:false}")
@SuppressWarnings("UnnecessarilyFullyQualified")
public class RabbitMQConfiguration {

    private final Logger logger = LoggerFactory.getLogger(RabbitMQConfiguration.class);

    @Value("${repo.messaging.username:guest}")
    private String username;
    @Value("${repo.messaging.password:guest}")
    private String password;

    @Value("${repo.messaging.hostname:localhost}")
    private String hostname;
    @Value("${repo.messaging.port:5672}")
    private int port;
    @Value("${repo.messaging.sender.exchange:repository_events}")
    private String exchange;

    @Value("${repo.messaging.enabled:FALSE}")
    private boolean messagingEnabled;

    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        logger.trace("Connecting to RabbitMQ service at host {} and port {}.", hostname, port);
        CachingConnectionFactory factory = new CachingConnectionFactory(hostname, port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitMQTemplate() {
        logger.trace("Get RabbitMQ template");
        return new RabbitTemplate(rabbitMQConnectionFactory());
    }

    @Bean
    public TopicExchange rabbitMQExchange() {
        logger.trace("Get Topic Exchange '{}'", exchange);
        return new TopicExchange(exchange);
    }
}
