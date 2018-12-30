package sihoiba.interviewHomework.config;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger( ApplicationConfig.class );

    @Bean
    public EnvironmentSettings environmentSettings( Environment env ) {
        return new EnvironmentSettings( env );
    }

    @Bean
    public RestTemplate restTemplate( RestTemplateBuilder restTemplateBuilder ) {
        return restTemplateBuilder.build();
    }

    @Bean
    public YouTube youtube() {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = new NetHttpTransport();
        YouTube.Builder youtubeBuilder = new YouTube.Builder( httpTransport, jsonFactory, null );
        return youtubeBuilder.setApplicationName( "interview application" ).build();
    }


}
