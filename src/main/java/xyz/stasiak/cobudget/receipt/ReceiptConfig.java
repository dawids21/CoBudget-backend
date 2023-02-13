package xyz.stasiak.cobudget.receipt;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = ReceiptConfigurationProperties.class)
class ReceiptConfig {
    @Bean
    ReceiptService receiptService(ReceiptConfigurationProperties receiptConfigurationProperties) {
        return new ReceiptService(receiptConfigurationProperties);
    }
}
