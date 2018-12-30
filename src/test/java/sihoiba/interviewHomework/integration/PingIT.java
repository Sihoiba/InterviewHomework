package sihoiba.interviewHomework.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import sihoiba.interviewHomework.controller.ControllerErrorHandler;
import sihoiba.interviewHomework.controller.PingController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Integration tests for the Ping Controller endpoint.
 */
@RunWith( SpringRunner.class )
@WebMvcTest( controllers = { PingController.class, ControllerErrorHandler.class }, secure = false )
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class PingIT {

    private static final String PING_ENDPOINT = "/ping";
    private static final String PING_CONTENT_TYPE = MediaType.TEXT_PLAIN_VALUE + ";charset=ISO-8859-1";
    private static final String PING_BODY = "OK";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate mockRestTemplate;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup( context ).build();
        reset( mockRestTemplate );
    }

    @Test
    public void shouldGetPing() throws Exception {
        // Given
        RequestBuilder request = get( PING_ENDPOINT ).accept( MediaType.TEXT_PLAIN );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( response.getContentAsString() ).isEqualTo( PING_BODY );
        assertThat( response.getHeader( HttpHeaders.CONTENT_TYPE ) ).isEqualTo( PING_CONTENT_TYPE );
    }

    @Test
    public void shouldGetPingGivenNoAcceptHeader() throws Exception {
        // Given
        RequestBuilder request = get( PING_ENDPOINT );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( response.getContentAsString() ).isEqualTo( PING_BODY );
        assertThat( response.getHeader( HttpHeaders.CONTENT_TYPE ) ).isEqualTo( PING_CONTENT_TYPE );
    }

    @Test
    public void shouldGetOptions() throws Exception {
        // Given
        RequestBuilder request = options( PING_ENDPOINT );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( response.getContentAsString() ).isEqualTo( "" );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET,HEAD,OPTIONS" );
    }

    @Test
    public void shouldGetHead() throws Exception {
        // Given
        RequestBuilder request = head( PING_ENDPOINT );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
    }

    @Test
    public void shouldFailGivenPostMethodUsed() throws Exception {
        // Given
        RequestBuilder request = post( PING_ENDPOINT )
                .contentType( MediaType.TEXT_PLAIN )
                .content( "something" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'POST' not supported"  );
    }

    @Test
    public void shouldFailGivenPutMethodUsed() throws Exception {
        // Given
        RequestBuilder request = put( PING_ENDPOINT )
                .contentType( MediaType.TEXT_PLAIN )
                .content( "something" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'PUT' not supported"  );
    }

    @Test
    public void shouldFailGivenDeleteMethodUsed() throws Exception {
        // Given
        RequestBuilder request = delete( PING_ENDPOINT );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then

        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'DELETE' not supported"  );
    }

    @Test
    public void shouldFailGivenUnacceptableMediaType() throws Exception {
        // Given
        RequestBuilder request = get( PING_ENDPOINT ).accept( MediaType.APPLICATION_ATOM_XML );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.NOT_ACCEPTABLE.value() );
    }


}
