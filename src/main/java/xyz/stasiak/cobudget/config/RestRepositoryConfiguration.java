package xyz.stasiak.cobudget.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Configuration
class RestRepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(new StringToLocalDateConverter());
    }

    private static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(@NonNull String source) {
            return LocalDate.parse(source);
        }
    }

}
