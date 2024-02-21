package com.rocky.reservationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfigure {
    @Bean
    public NewTopic seatTopic(){
        return TopicBuilder
                .name("roomTopic")
                .build();
    }
}
