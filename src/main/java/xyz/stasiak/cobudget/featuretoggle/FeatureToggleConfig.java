package xyz.stasiak.cobudget.featuretoggle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.stasiak.cobudget.security.SecurityService;

@Configuration
class FeatureToggleConfig {
    @Bean
    FeatureToggleService featureToggleService(FeatureToggleRepository featureToggleRepository,
                                              SecurityService securityService) {
        return new FeatureToggleService(featureToggleRepository, securityService);
    }
}
