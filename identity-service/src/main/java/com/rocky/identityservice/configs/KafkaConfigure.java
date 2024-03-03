package com.rocky.identityservice.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfigure {
    @Bean
    public NewTopic sendMailForgetTopic(){
        return TopicBuilder
                .name("sendMailForgetTopic")
                .build();
    }

    @Bean
    public NewTopic cleanForgetCodeTopic(){
        return TopicBuilder
                .name("cleanForgetCodeTopic")
                .build();
    }

}
