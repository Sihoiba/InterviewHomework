package sihoiba.interviewHomework.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sihoiba.interviewHomework.model.SearchTermType;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.model.VideoDetailsSearchResponse;
import sihoiba.interviewHomework.model.VideoDetailsSearchResult;
import sihoiba.interviewHomework.service.YoutubeVideoDetailsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit test for {@link VideoDetailsController}
 */
@RunWith( MockitoJUnitRunner.class )
public class VideoDetailsControllerTest {

    @Mock
    YoutubeVideoDetailsService mockYoutubeVideoDetailService;

    @InjectMocks
    VideoDetailsController classUnderTest;

    @Test
    public void shouldTriggerPopulateVideoDetails() {
        // Given
        willDoNothing().given( mockYoutubeVideoDetailService ).populateVideoDetails();

        // When
        classUnderTest.populateVideoDetails();

        // Then
        then( mockYoutubeVideoDetailService ).should().populateVideoDetails();

    }

    @Test
    public void shouldGetAllResults() {
        // Given
        List<Video> expectedVideoList = new ArrayList<>();
        Video video1 = givenAVideoDetails( 11L );
        Video video2 = givenAVideoDetails( 12L );
        expectedVideoList.add( video1 );
        expectedVideoList.add( video2 );
        given( mockYoutubeVideoDetailService.getAllStoredVideoDetails() ).willReturn( expectedVideoList );

        //When
        List<Video> result = classUnderTest.getAllVideoDetails();

        //Then
        assertThat( result ).isEqualTo( expectedVideoList );
        then( mockYoutubeVideoDetailService ).should().getAllStoredVideoDetails();
    }

    @Test
    public void shouldGetVideoDetails() throws Exception {
        // Given
        Long id = 10L;
        Video expectedVideo = givenAVideoDetails( id );
        given( mockYoutubeVideoDetailService.getVideoDetails( anyLong() ) ).willReturn( expectedVideo );

        //When
        Video result = classUnderTest.getVideoDetailsById( id );

        //Then
        assertThat( result ).isEqualTo( expectedVideo );
        then( mockYoutubeVideoDetailService ).should().getVideoDetails( id );
    }

    @Test
    public void shouldNotGetVideoDetailsGivenNullId() throws Exception {
        // Given
        Long id = null;

        // When
        try {
            classUnderTest.getVideoDetailsById( id );
            shouldHaveThrown( IllegalArgumentException.class );
            // Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "id must not be null." );
            verifyZeroInteractions( mockYoutubeVideoDetailService );
        }

    }

    @Test
    public void shouldDeleteVideoDetails() {
        // Given
        Long id = 10L;
        willDoNothing().given( mockYoutubeVideoDetailService ).deleteVideoDetails( anyLong() );

        // When
        classUnderTest.deleteVideoDetails( id );

        //Then
        then( mockYoutubeVideoDetailService ).should().deleteVideoDetails( id );

    }

    @Test
    public void shouldNotDeleteVideoDetailsGivenNullId() {
        // Given
        Long id = null;

        // When
        try {
            classUnderTest.deleteVideoDetails( id );
            shouldHaveThrown( IllegalArgumentException.class );
            //Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "id must not be null." );
            verifyZeroInteractions( mockYoutubeVideoDetailService );
        }
    }

    @Test
    public void shouldFindMatchingGivenSearchTerm() {
        //Given
        SearchTermType searchTerm = SearchTermType.TITLE;
        String value = "cycling";
        List<VideoDetailsSearchResult> videosReturned = new ArrayList<>();
        given( mockYoutubeVideoDetailService.searchVideos( searchTerm, value ) ).willReturn( videosReturned );

        //When
        VideoDetailsSearchResponse response = classUnderTest.searchVideoDetails( searchTerm, value );

        //Then
        assertThat( response ).isNotNull();
        assertThat( response.getResults() ).containsExactlyElementsOf( videosReturned );
        then( mockYoutubeVideoDetailService ).should().searchVideos( searchTerm, value );
        verifyNoMoreInteractions( mockYoutubeVideoDetailService );

    }

    @Test
    public void shouldNotFindMatchingGivenNullSearchTermType() {
        // Given
        SearchTermType searchTerm = null;
        String value = "cycling";

        // When
        try {
            classUnderTest.searchVideoDetails( searchTerm, value );
            shouldHaveThrown( IllegalArgumentException.class );
            //Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "searchTermType must not be null." );
            verifyZeroInteractions( mockYoutubeVideoDetailService );
        }
    }

    @Test
    public void shouldNotFindMatchingGivenNullSearchValue() {
        // Given
        SearchTermType searchTerm = SearchTermType.TITLE;
        String value = null;

        // When
        try {
            classUnderTest.searchVideoDetails( searchTerm, value );
            shouldHaveThrown( IllegalArgumentException.class );
            //Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "value must not be null." );
            verifyZeroInteractions( mockYoutubeVideoDetailService );
        }
    }

    private Video givenAVideoDetails( Long id ) {
        return new Video( id, "test", LocalDateTime.now() );
    }
}
