package sihoiba.interviewHomework.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration for the interview homework application
 */
@Configuration
@ComponentScan( basePackages = { "sihoiba.interviewHomework.service" } )
public class ApplicationConfig {

    @Bean
    public EnvironmentSettings environmentSettings( Environment env ) {
        return new EnvironmentSettings( env );
    }

    @Bean
    public RestTemplate restTemplate( RestTemplateBuilder restTemplateBuilder ) {
        return restTemplateBuilder.build();
    }



}
