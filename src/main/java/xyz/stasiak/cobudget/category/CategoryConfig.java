package xyz.stasiak.cobudget.category;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CategoryConfig {

    @Bean
    CategoryApplicationService categoryApplicationService(CategoryRepository repository) {
        return new CategoryApplicationService(repository);
    }

}
