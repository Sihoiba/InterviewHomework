package sihoiba.interviewHomework.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sihoiba.interviewHomework.client.YouTubeClient;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.persistence.VideosDao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

	private static final LocalDateTime CURRENT_TIME = LocalDateTime.now().withNano( 0 );

	@Mock
	VideosDao mockVideosDao;

	@Mock
	YouTubeClient mockYouTubeClient;

	@InjectMocks
	YoutubeVideoDetailsService classUnderTest;

	@Test
	public void shouldPopulateVideoDetails() {
		// Given
		SearchResultSnippet returnVideoSnippet1 = getSearchResultSnippet( "test title 1" );
		SearchResultSnippet returnVideoSnippet2 = getSearchResultSnippet( "test title 2" );
		List<SearchResultSnippet> returnedVideos = new ArrayList<>();
		returnedVideos.add( returnVideoSnippet1 );
		returnedVideos.add( returnVideoSnippet2 );
		given( mockYouTubeClient.getVideoDetails() ).willReturn( returnedVideos );
		willDoNothing().given( mockVideosDao ).create( isA(  Video.class ) );

		// When
		classUnderTest.populateVideoDetails();

		// Then
		then( mockYouTubeClient ).should().getVideoDetails();
		ArgumentCaptor<Video> videoArgumentCaptor =
				ArgumentCaptor.forClass( Video.class );
		then( mockVideosDao ).should( times( 2 ) ).create( videoArgumentCaptor.capture() );
		Video actualParam1 = videoArgumentCaptor.getAllValues().get( 0 );
		Video actualParam2 = videoArgumentCaptor.getAllValues().get( 1 );
		assertThat( actualParam1 )
				.hasFieldOrPropertyWithValue( "id", null )
				.hasFieldOrPropertyWithValue( "title", "test title 1" )
				.hasFieldOrPropertyWithValue( "publishedAt", CURRENT_TIME );
		assertThat( actualParam2 )
				.hasFieldOrPropertyWithValue( "id", null )
				.hasFieldOrPropertyWithValue( "title", "test title 2" )
				.hasFieldOrPropertyWithValue( "publishedAt", CURRENT_TIME );
		verifyNoMoreInteractions( mockVideosDao, mockYouTubeClient );
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
		verifyZeroInteractions( mockYouTubeClient );
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
		verifyZeroInteractions( mockYouTubeClient );
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
		verifyZeroInteractions( mockYouTubeClient );

	}

	private SearchResultSnippet getSearchResultSnippet( String title ) {
		SearchResultSnippet searchResultSnippet = new SearchResultSnippet();
		Date date = Date.from(CURRENT_TIME.atZone(ZoneId.systemDefault()).toInstant());
		searchResultSnippet.setPublishedAt( new DateTime( date ) );
		searchResultSnippet.setTitle( title );
		return searchResultSnippet;
	}

	private Video getInternalVideo( long id, String title ) {
		return new Video( id, title, CURRENT_TIME );
	}

}
