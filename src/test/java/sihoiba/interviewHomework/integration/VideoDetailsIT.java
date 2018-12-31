package sihoiba.interviewHomework.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import sihoiba.interviewHomework.model.SearchTermType;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.model.VideoDetailsSearchResponse;
import sihoiba.interviewHomework.persistence.VideosRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class VideoDetailsIT {

    private static final LocalDateTime CURRENT_TIME = LocalDateTime.now().withNano( 0 );

    @Autowired
    private VideosRepository videosRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YouTubeClient mockYouTubeClient;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private RestTemplate mockRestTemplate;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup( context ).build();
        reset( mockRestTemplate );
        reset( mockYouTubeClient );
    }

    @Test
    public void shouldPopulateVideoDetails() throws Exception {
        // Given
        RequestBuilder request = post( "/populateVideos" );
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
        List<Video> storedVideos = videosRepository.findAll();
        assertThat( storedVideos ).hasSize( 2 );
        assertThat( storedVideos ).extracting( "title" ).containsExactly( "test title 1", "test title 2" );
    }

    @Test
    public void shouldFailToPopulateGivenGetMethodUsed() throws Exception {
        // Given
        RequestBuilder request = get( "/populateVideos" ).accept( MediaType.APPLICATION_JSON_VALUE );

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
        RequestBuilder request = put( "/populateVideos" )
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
        RequestBuilder request = delete( "/populateVideos" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'DELETE' not supported" );
    }

    @Test
    public void shouldGetAllVideoDetails() throws Exception {
        //Given
        LocalDateTime localDateTime1 = LocalDateTime.now().withNano( 0 );
        Video video1 = new Video( "title 1", localDateTime1 );
        LocalDateTime localDateTime2 = LocalDateTime.now().withNano( 0 );
        Video video2 = new Video( "title 2", localDateTime2 );
        videosRepository.saveAll( Arrays.asList( video1, video2 ) );
        RequestBuilder request = get( "/videos" ).accept( MediaType.APPLICATION_JSON_VALUE );

        //When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        //Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        List<Video> retrievedVideos = Arrays.asList( OBJECT_MAPPER.readValue( response.getContentAsString(), Video[].class ) );
        assertThat( retrievedVideos ).hasSize( 2 );
        assertThat( retrievedVideos ).extracting( "title" ).containsExactly( "title 1", "title 2" );
        assertThat( retrievedVideos ).extracting( "publishedAt" ).containsExactly( localDateTime1, localDateTime2 );
    }

    @Test
    public void shouldFailToGetAllVideosGivenPostMethodUsed() throws Exception {
        // Given
        RequestBuilder request = post( "/videos" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'POST' not supported" );
    }

    @Test
    public void shouldFailToGetAllVideosGivenPutMethodUsed() throws Exception {
        // Given
        RequestBuilder request = put( "/videos" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'PUT' not supported" );
    }

    @Test
    public void shouldFailToGetAllVideosGivenDeleteMethodUsed() throws Exception {
        // Given
        RequestBuilder request = delete( "/videos" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'DELETE' not supported" );
    }

    @Test
    public void shouldGetVideoById() throws Exception {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now().withNano( 0 );
        Video video = new Video( "title 1", localDateTime );
        Video persisted = videosRepository.save( video );
        RequestBuilder request = get( "/videos/" + persisted.getId() ).accept( MediaType.APPLICATION_JSON_VALUE );

        //When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        //Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        Video retrievedVideo = OBJECT_MAPPER.readValue( response.getContentAsString(), Video.class );
        assertThat( retrievedVideo ).isEqualTo( persisted );
    }

    @Test
    public void shouldDeleteVideoById() throws Exception {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now().withNano( 0 );
        Video video = new Video( "title 1", localDateTime );
        Video persisted = videosRepository.save( video );
        assertThat( videosRepository.findAll() ).hasSize( 1 );
        RequestBuilder request = delete( "/videos/" + persisted.getId() );

        //When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        //Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        assertThat( videosRepository.findAll() ).hasSize( 0 );
    }

    @Test
    public void shouldFailToGetOrDeleteVideoByIdGivenPostMethodUsed() throws Exception {
        // Given
        RequestBuilder request = post( "/videos/1" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).contains( "DELETE" ).contains( "GET" ).doesNotContain( "PUT", "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'POST' not supported" );
    }

    @Test
    public void shouldFailToGetOrDeleteVideoByIdGivenPutMethodUsed() throws Exception {
        // Given
        RequestBuilder request = put( "/videos/1" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).contains( "DELETE" ).contains( "GET" ).doesNotContain( "PUT", "POST" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'PUT' not supported" );
    }

    @Test
    public void shouldSearchVideosGivenTitleSearchTerm() throws Exception {
        LocalDateTime localDateTime1 = LocalDateTime.now().withNano( 0 );
        Video video1 = new Video( "matching title 1", localDateTime1 );
        LocalDateTime localDateTime2 = LocalDateTime.now().withNano( 0 );
        Video video2 = new Video( "no match title 2", localDateTime2 );
        LocalDateTime localDateTime3 = LocalDateTime.now().withNano( 0 );
        Video video3 = new Video( " title MATCHING 3", localDateTime3 );
        videosRepository.saveAll( Arrays.asList( video1, video2, video3 ) );

        SearchTermType searchTermType = SearchTermType.TITLE;
        String searchValue = "Matching";
        RequestBuilder request = get( String.format( "/searchVideos?searchTermType=%s&value=%s", searchTermType, searchValue )  ).accept( MediaType.APPLICATION_JSON );

        //When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        //Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
        VideoDetailsSearchResponse videoDetailsSearchResponse = OBJECT_MAPPER.readValue( response.getContentAsString(), VideoDetailsSearchResponse.class );
        assertThat( videoDetailsSearchResponse.getResults() ).hasSize( 2 );
        assertThat( videoDetailsSearchResponse.getResults() ).extracting( "id" ).containsExactly( video1.getId(), video3.getId() );
        assertThat( videoDetailsSearchResponse.getResults() ).extracting( "title" ).containsExactly( video1.getTitle(), video3.getTitle() );
    }

    @Test
    public void shouldFailToSearchVideosGivenPostMethodUsed() throws Exception {
        // Given
        RequestBuilder request = post( "/searchVideos" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'POST' not supported" );
    }

    @Test
    public void shouldFailToSearchVideosGivenPutMethodUsed() throws Exception {
        // Given
        RequestBuilder request = put( "/searchVideos" ).accept( MediaType.APPLICATION_JSON_VALUE );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
        assertThat( response.getErrorMessage() ).isEqualTo( "Request method 'PUT' not supported" );
    }

    @Test
    public void shouldFailToSearchVideosGivenDeleteMethodUsed() throws Exception {
        // Given
        RequestBuilder request = delete( "/searchVideos" );

        // When
        MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();

        // Then
        assertThat( response.getStatus() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
        assertThat( response.getHeader( HttpHeaders.ALLOW ) ).isEqualTo( "GET" );
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
