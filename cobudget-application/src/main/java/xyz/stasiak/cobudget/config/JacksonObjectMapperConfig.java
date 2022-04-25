package xyz.stasiak.cobudget.config;

import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonObjectMapperConfig {

    @Bean
    VavrModule vavrModule() {
        return new VavrModule();
    }
}
