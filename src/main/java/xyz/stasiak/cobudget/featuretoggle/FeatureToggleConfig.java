package xyz.stasiak.cobudget.featuretoggle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureToggleConfig {
    @Bean
    FeatureToggleService featureToggleService(FeatureToggleRepository featureToggleRepository) {
        return new FeatureToggleService(featureToggleRepository);
    }
}
