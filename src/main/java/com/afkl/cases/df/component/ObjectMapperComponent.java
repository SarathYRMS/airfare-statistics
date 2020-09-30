package com.afkl.cases.df.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperComponent {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
