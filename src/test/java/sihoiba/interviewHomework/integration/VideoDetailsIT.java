package sihoiba.interviewHomework.integration;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import sihoiba.interviewHomework.client.YouTubeClient;
import sihoiba.interviewHomework.controller.ControllerErrorHandler;
import sihoiba.interviewHomework.controller.VideoDetailsController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Integration tests for the VideoDetails Controller endpoint.
 */
@RunWith( SpringRunner.class )
@WebMvcTest( controllers = { VideoDetailsController.class, ControllerErrorHandler.class }, secure = false )
@SpringBootTest
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class VideoDetailsIT {

    private static final LocalDateTime CURRENT_TIME = LocalDateTime.now().withNano( 0 );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YouTubeClient mockYouTubeClient;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private RestTemplate mockRestTemplate;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup( context ).build();
        reset( mockRestTemplate );
        reset( mockYouTubeClient );
    }

    @Test
    public void shouldPopulateVideoDetails() throws Exception {
        // Given
        RequestBuilder request = post( "/videos/populate" );
        SearchResultSnippet returnVideoSnippet1 = getSearchResultSnippet( "test title 1" );
        SearchResultSnippet returnVideoSnippet2 = getSearchResultSnippet( "test title 2" );
        List<SearchResultSnippet> returnedVideos = new ArrayList<>();
        returnedVideos.add( returnVideoSnippet1 );
        returnedVideos.add( returnVideoSnippet2 );
        given( mockYouTubeClient.getVideoDetails() ).willReturn( returnedVideos );


        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
    }

    @Test
    public void shouldFailToPopulateGivenGetMethodUsed() throws Exception {
        // Given
        RequestBuilder request = get( "/videos/populate" ).accept( MediaType.APPLICATION_JSON );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'GET' not supported" );
    }

    @Test
    public void shouldFailToPopulateGivenPostMethodUsed() throws Exception {
        // Given
        RequestBuilder request = put( "/videos/populate" )
                .contentType( MediaType.TEXT_PLAIN )
                .content( "something" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'PUT' not supported" );
    }

    @Test
    public void shouldFailToPopulateGivenDeleteMethodUsed() throws Exception {
        // Given
        RequestBuilder request = delete( "/videos/populate" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'DELETE' not supported" );
    }

    private SearchResultSnippet getSearchResultSnippet( String title ) {
        SearchResultSnippet searchResultSnippet = new SearchResultSnippet();
        Date date = Date.from( CURRENT_TIME.atZone( ZoneId.systemDefault() ).toInstant() );
        searchResultSnippet.setPublishedAt( new DateTime( date ) );
        searchResultSnippet.setTitle( title );
        return searchResultSnippet;
    }
}
