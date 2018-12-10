package sihoiba.interviewHomework.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sihoiba.interviewHomework.model.VideoDetails;
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
import static org.mockito.Mockito.verifyZeroInteractions;


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
        List<VideoDetails> expectedVideoDetailsList = new ArrayList<>();
        VideoDetails videoDetails1 = givenAVideoDetails( 11L );
        VideoDetails videoDetails2 = givenAVideoDetails( 12L );
        expectedVideoDetailsList.add( videoDetails1 );
        expectedVideoDetailsList.add( videoDetails2 );
        given( mockYoutubeVideoDetailService.getAllStoredVideoDetails() ).willReturn( expectedVideoDetailsList );

        //When
        List<VideoDetails> result = classUnderTest.getAllVideoDetails();

        //Then
        assertThat( result ).isEqualTo( expectedVideoDetailsList );
        then( mockYoutubeVideoDetailService ).should().getAllStoredVideoDetails();
    }

    @Test
    public void shouldGetVideoDetails() {
        // Given
        Long id = 10L;
        VideoDetails expectedVideoDetails = givenAVideoDetails( id );
        given( mockYoutubeVideoDetailService.getVideoDetails( anyLong() ) ).willReturn( expectedVideoDetails );

        //When
        VideoDetails result = classUnderTest.getVideoDetailsById( id );

        //Then
        assertThat( result ).isEqualTo( expectedVideoDetails );
        then( mockYoutubeVideoDetailService ).should().getVideoDetails( id );
    }

    @Test
    public void shouldNotGetVideoDetailsGivenNullId() {
        // Given
        Long id = null;

        // When
        try {
            classUnderTest.getVideoDetailsById( id );
            shouldHaveThrown( NullPointerException.class );
            // Then
        } catch( NullPointerException expected ) {
            assertThat( expected ).hasMessage( "id must not be null" );
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
            shouldHaveThrown( NullPointerException.class );
            //Then
        } catch( NullPointerException expected ) {
            assertThat( expected ).hasMessage( "id must not be null" );
            verifyZeroInteractions( mockYoutubeVideoDetailService );
        }
    }

    @Test
    public void shouldFindMatchingGivenSearchTerm() {

    }

    @Test
    public void shouldNotFindMatchingGivenNullSearchTerm() {

    }

    private VideoDetails givenAVideoDetails( Long id ) {
        return new VideoDetails( id, "test", LocalDateTime.now() );
    }
}
