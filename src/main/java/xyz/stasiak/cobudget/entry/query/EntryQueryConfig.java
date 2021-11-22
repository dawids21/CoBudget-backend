package xyz.stasiak.cobudget.entry.query;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EntryQueryConfig {

    @Bean
    EntryQueryService entryQueryService(EntryQueryRepository repository) {
        return new EntryQueryService(repository);
    }

}
