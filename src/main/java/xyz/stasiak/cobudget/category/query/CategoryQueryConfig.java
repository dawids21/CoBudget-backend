package xyz.stasiak.cobudget.category.query;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CategoryQueryConfig {

    @Bean
    CategoryQueryService categoryQueryService(CategoryQueryRepository repository) {
        return new CategoryQueryService(repository);
    }

}
