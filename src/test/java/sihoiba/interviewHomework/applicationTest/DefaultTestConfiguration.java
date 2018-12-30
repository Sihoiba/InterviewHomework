package sihoiba.interviewHomework.applicationTest;

import com.google.api.services.youtube.YouTube;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sihoiba.interviewHomework.client.YouTubeClient;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@TestConfiguration
@ComponentScan( basePackages = { "sihoiba.interviewHomework.service", "sihoiba.interviewHomework.controller", "sihoiba.interviewHomework.persistence"} )
@EnableJpaRepositories
public class DefaultTestConfiguration {

    @Bean
    public MockMvc mockMvc( WebApplicationContext context ) {
        return MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Bean
    public YouTube youTube() {
        return mock( YouTube.class );
    }

    @Bean
    public YouTubeClient youTubeClient() {
        return mock( YouTubeClient.class );
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType( EmbeddedDatabaseType.H2 )
                .ignoreFailedDrops( true )
                .generateUniqueName( true )
                .setName( "mydb" )
                .setScriptEncoding( "UTF-8" )
                .addScripts( "/youtube.sql" )
                .build();
    }
}
