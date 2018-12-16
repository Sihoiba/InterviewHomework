package sihoiba.interviewHomework.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.VideoSnippet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sihoiba.interviewHomework.client.YoutubeClient;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.persistence.VideosDao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit test for {@link YoutubeVideoDetailsService}
 */
@RunWith( MockitoJUnitRunner.class )
public class YoutubeVideoDetailsServiceTest {

	private static final LocalDateTime CURRENT_TIME = LocalDateTime.now();
	private static final String TITLE = "Test video title";

	@Mock
	VideosDao mockVideosDao;

	@Mock
	YoutubeClient mockYoutubeClient;

	@InjectMocks
	YoutubeVideoDetailsService classUnderTest;

	@Test
	public void shouldPopulateVideoDetails() {
		// Given
		com.google.api.services.youtube.model.Video returnedVideo1 = getYoutubeVideo( "test title 1" );
		com.google.api.services.youtube.model.Video returnedVideo2 = getYoutubeVideo( "test title 2" );
		List<com.google.api.services.youtube.model.Video> returnedVideos = new ArrayList<>();
		returnedVideos.add( returnedVideo1 );
		returnedVideos.add( returnedVideo2 );
		given( mockYoutubeClient.getVideoDetails() ).willReturn( returnedVideos );
		willDoNothing().given( mockVideosDao ).create( isA(  Video.class ) );

		// When
		classUnderTest.populateVideoDetails();

		// Then
		then( mockYoutubeClient ).should().getVideoDetails();
		ArgumentCaptor<Video> videoArgumentCaptor =
				ArgumentCaptor.forClass( Video.class );
		then( mockVideosDao ).should( times( 2 ) ).create( videoArgumentCaptor.capture() );
		Video actualParam1 = videoArgumentCaptor.getAllValues().get( 0 );
		Video actualParam2 = videoArgumentCaptor.getAllValues().get( 0 );
		assertThat( actualParam1 )
				.hasNoNullFieldsOrProperties()
				.hasFieldOrPropertyWithValue( "title", "test title 1" )
				.hasFieldOrPropertyWithValue( "publishedAt", CURRENT_TIME );
		assertThat( actualParam2 )
				.hasNoNullFieldsOrProperties()
				.hasFieldOrPropertyWithValue( "title", "test title 2" )
				.hasFieldOrPropertyWithValue( "publishedAt", CURRENT_TIME );
		verifyNoMoreInteractions( mockVideosDao, mockYoutubeClient );
	}

	@Test
	public void shouldGetAllStoredVideoDetails() {
		// Given
		Video video1 = getInternalVideo( 1L, "test video 1" );
		Video video2 = getInternalVideo( 2L, "test video 2" );
		List<Video> storedVideos = new ArrayList<>();
		storedVideos.add( video1 );
		storedVideos.add( video2 );
		given( mockVideosDao.getAllVideos() ).willReturn( storedVideos );

		// When
		List<Video> result = classUnderTest.getAllStoredVideoDetails();

		// Then
		assertThat( result ).containsExactlyElementsOf( storedVideos );
		then( mockVideosDao ).should().getAllVideos();
		verifyNoMoreInteractions( mockVideosDao );
		verifyZeroInteractions( mockYoutubeClient );
	}

	@Test
	public void shouldGetVideoDetails() {
		// Given
		Video video = getInternalVideo( 1L, "test video 1" );
		given( mockVideosDao.get( 1L ) ).willReturn( video );

		// When
		Video result = classUnderTest.getVideoDetails( 1L );

		// Then
		assertThat( result ).isEqualTo( video );
		then( mockVideosDao ).should().get( 1L );
		verifyNoMoreInteractions( mockVideosDao );
		verifyZeroInteractions( mockYoutubeClient );
	}

	@Test
	public void shouldDeleteVideoDetails() {
		// Given
		willDoNothing().given( mockVideosDao ).delete( 1L );

		// When
		classUnderTest.deleteVideoDetails( 1L );

		// Then
		then( mockVideosDao ).should().delete( 1L );
		verifyNoMoreInteractions( mockVideosDao );
		verifyZeroInteractions( mockYoutubeClient );

	}

	private com.google.api.services.youtube.model.Video getYoutubeVideo( String title ) {
		com.google.api.services.youtube.model.Video returnedVideo = new com.google.api.services.youtube.model.Video();
		VideoSnippet videoSnippet = new VideoSnippet();
		videoSnippet.setPublishedAt( new DateTime( CURRENT_TIME.toEpochSecond( ZoneOffset.UTC ) ) );
		videoSnippet.setTitle( title );
		returnedVideo.setSnippet( videoSnippet );
		return returnedVideo;
	}

	private Video getInternalVideo( long id, String title ) {
		return new Video( id, title, CURRENT_TIME );
	}

}
