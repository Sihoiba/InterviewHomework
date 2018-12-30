package sihoiba.interviewHomework.applicationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import sihoiba.interviewHomework.client.YouTubeClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full Spring Boot test for the {@link sihoiba.interviewHomework.InterviewHomeworkApplication} application. This test starts a real service, the
 * tests make real HTTP requests to the locally running server.
 */
@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@TestPropertySource( locations = "classpath:test.properties" )
@DirtiesContext
@EnableAutoConfiguration
public class ApplicationAppIT {

    private static final Logger LOG = LoggerFactory.getLogger( ApplicationAppIT.class );

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private YouTubeClient mockYouTubeClient;

    @Test
    public void shouldPing() {
        // Given

        // When
        String response = ping( restTemplate );

        // Then
        assertThat( response ).isEqualTo( "OK" );
    }

    private String ping( TestRestTemplate restTemplate ) {
        LOG.info( "Sending ping request" );
        String pingResponse = restTemplate.getForObject( "/ping", String.class );
        LOG.info( "Ping response: {}", pingResponse );
        return pingResponse;
    }
}
