package xyz.stasiak.cobudget.plan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlanConfig {

    @Bean
    PlanApplicationService planApplicationService(PlanRepository planRepository) {
        return new PlanApplicationService(planRepository);
    }
}
