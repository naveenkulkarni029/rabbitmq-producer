package org.nbk.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

    @Bean
    Queue queue() {
	return QueueBuilder.durable("events.queue").withArgument("x-dead-letter-exchange", "deadLetter.events.exchange")
		.withArgument("x-dead-letter-routing-key", "deadLetter").build();
    }

    @Bean
    Queue dlq() {
	return  QueueBuilder.durable("deadLetter.queue").withArgument("x-message-ttl", 5000).build();
    }

    @Bean
    DirectExchange exchange() {
	return new DirectExchange("events.exchange");
    }

    @Bean
    DirectExchange deadLetterExchange() {
	return new DirectExchange("deadLetter.events.exchange");
    }

    @Bean
    Binding binding() {
	return BindingBuilder.bind(queue()).to(exchange()).with("events");
    }
    
    @Bean
    Binding deadLetterBinding() {
	return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with("deadLetter");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
	return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	rabbitTemplate.setMessageConverter(jsonMessageConverter());
	return rabbitTemplate;
    }

}
