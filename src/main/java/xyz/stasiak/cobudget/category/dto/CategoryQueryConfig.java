package xyz.stasiak.cobudget.category.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CategoryQueryConfig {

    @Bean
    CategoryQueryService categoryQueryService(CategoryQueryRepository repository) {
        return new CategoryQueryService(repository);
    }

}
