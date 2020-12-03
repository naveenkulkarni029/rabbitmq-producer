package org.nbk.rabbitmq.producer.controller;

import org.nbk.rabbitmq.producer.domain.Employee;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitmqTemplate;

    @PostMapping(value = "post", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postMessage(@RequestBody Employee employee) {

	rabbitmqTemplate.convertAndSend("events.exchange", "events", employee);

	return ResponseEntity.ok().body(null);
    }

}
