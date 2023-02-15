package xyz.stasiak.cobudget.receipt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cobudget.receipts")
record ReceiptConfigurationProperties(String bucket) {

}
