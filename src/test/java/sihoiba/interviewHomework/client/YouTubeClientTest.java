package sihoiba.interviewHomework.client;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sihoiba.interviewHomework.exception.ApplicationException;
import sihoiba.interviewHomework.persistence.ChannelsDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit test for {@link YouTubeClient}
 */
@RunWith( MockitoJUnitRunner.class )
public class YouTubeClientTest {

    private static final String API_KEY = "api_key";
    private static final String GLOBAL_CYCLING_NETWORK_CHANNEL_ID = "UCuTaETsuCOkJ0H_GAztWt0Q";
    private static final String GLOBALMTB_CHANNEL_ID = "UC_A--fhX5gea0i4UtpD99Gg";

    @Mock
    private ChannelsDao mockChannelsDao;

    @Mock
    private YouTube mockYouTube;

    @InjectMocks
    private YouTubeClient classUnderTest;

    @Before
    public void setup() {
        classUnderTest.setApiKey( API_KEY );
    }

    @Test
    public void shouldGetVideoDetails() throws Exception {
        // Given
        YouTube.Search mockSearch = mock( YouTube.Search.class );
        YouTube.Search.List mockSearchList = mock( YouTube.Search.List.class );

        SearchListResponse searchListResponse1Page1 = new SearchListResponse();
        searchListResponse1Page1.setNextPageToken( "NextPageToken1" );
        SearchResult searchResult1_1 = getSearchResult( "cycling PRO video 1" );
        SearchResult searchResult1_2 = getSearchResult( "Not wanted" );
        List<SearchResult> searchResults1Page1 = new ArrayList<>( Arrays.asList( searchResult1_1, searchResult1_2 ) );
        searchListResponse1Page1.setItems( searchResults1Page1 );

        SearchListResponse searchListResponse1Page2 = new SearchListResponse();
        searchListResponse1Page2.setNextPageToken( null );
        SearchResult searchResult2_1 = getSearchResult( "another video pro" );
        SearchResult searchResult2_2 = getSearchResult( "Not wanted 2" );
        List<SearchResult> searchResults1Page2 = new ArrayList<>( Arrays.asList( searchResult2_1, searchResult2_2 ) );
        searchListResponse1Page2.setItems( searchResults1Page2 );

        SearchListResponse searchListResponse2Page1 = new SearchListResponse();
        searchListResponse2Page1.setNextPageToken( null );
        SearchResult searchResult3 = getSearchResult( "Matt Stephens - does something cool" );
        List<SearchResult> searchResults2Page1 = new ArrayList<>( Arrays.asList( searchResult3 ));
        searchListResponse2Page1.setItems( searchResults2Page1 );

        //This set of mocks would be required if we could retrieve the channel IDs just using an API key
        //YouTube.Channels mockChannels = mock( YouTube.Channels.class );
        //YouTube.Channels.List mockChannelsList = mock( YouTube.Channels.List.class );
        //givenChannelIdsRetrieved( mockChannels, mockChannelsList );

        given( mockYouTube.search() ).willReturn( mockSearch ).willReturn( mockSearch ).willReturn( mockSearch ).willReturn( mockSearch );
        given( mockSearch.list( "snippet" ) ).willReturn( mockSearchList ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.setKey( API_KEY ) ).willReturn( mockSearchList ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.setMaxResults( 50L ) ).willReturn( mockSearchList ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.setPageToken( "NextPageToken1" ) ).willReturn( mockSearchList );
        given( mockSearchList.setChannelId( anyString() ) ).willReturn( mockSearchList ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.execute() ).willReturn( searchListResponse1Page1 ).willReturn( searchListResponse1Page2 ).willReturn( searchListResponse2Page1 );

        // When
        List<SearchResultSnippet> results = classUnderTest.getVideoDetails();

        // Then
        assertThat( results ).hasSize( 3 );
        assertThat( results ).extracting( "title").containsExactly( "cycling PRO video 1", "another video pro", "Matt Stephens - does something cool" );
        then( mockYouTube ).should( times( 3 ) ).search();
        then( mockSearch ).should( times( 3 ) ).list( "snippet" );
        then( mockSearchList ).should( times( 3 ) ).setKey( API_KEY );
        then( mockSearchList ).should( times( 3 ) ).setMaxResults( 50L );
        then( mockSearchList ).should().setPageToken(  "NextPageToken1" );
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass( String.class );
        then( mockSearchList ).should( times( 3 ) ).setChannelId( stringArgumentCaptor.capture() );
        assertThat( stringArgumentCaptor.getAllValues() ).containsExactlyInAnyOrder( GLOBAL_CYCLING_NETWORK_CHANNEL_ID, GLOBAL_CYCLING_NETWORK_CHANNEL_ID, GLOBALMTB_CHANNEL_ID );
        then( mockSearchList ).should( times( 3 ) ).execute();

        //This set of asserts would be required if we could retrieve the channel IDs just using an API key
        //verifyChannelMocksCalledCorrectly( mockChannels, mockChannelsList );
        verifyNoMoreInteractions( mockYouTube, mockSearch, mockSearchList );
    }

    @Test
    public void shouldFailToGetVideoDetailsGivenProblemSearchingVideos() throws IOException {
        // Given
        IOException thrownByTest = new IOException();
        YouTube.Search mockSearch = mock( YouTube.Search.class );
        given( mockYouTube.search() ).willReturn( mockSearch );
        given( mockSearch.list( "snippet" ) ).willThrow( thrownByTest );

        // When
        try {
            classUnderTest.getVideoDetails();
            shouldHaveThrown( ApplicationException.class );
            // Then
        } catch( ApplicationException expected ) {
            assertThat( expected ).hasMessage( "Failed to get video details from Youtube" );
            assertThat( expected ).hasCause( thrownByTest );
            then( mockYouTube ).should().search();
            then( mockSearch ).should().list( "snippet" );
            verifyNoMoreInteractions( mockYouTube, mockSearch );
        }
    }

    private void verifyChannelMocksCalledCorrectly( YouTube.Channels mockChannels, YouTube.Channels.List mockChannelsList ) throws IOException {
        then( mockYouTube ).should().channels();
        then( mockChannels ).should().list( "id, snippet" );
        then( mockChannelsList ).should().setKey( API_KEY );
        then( mockChannelsList ).should().execute();
        then( mockChannelsDao ).should().getAllChannels();
    }

    private void givenChannelIdsRetrieved( YouTube.Channels mockChannels, YouTube.Channels.List mockChannelsList ) throws IOException {
        sihoiba.interviewHomework.model.Channel wantedInternalChannel1 = new sihoiba.interviewHomework.model.Channel( 1L, "globalmtb" );
        sihoiba.interviewHomework.model.Channel wantedInternalChannel2 = new sihoiba.interviewHomework.model.Channel( 1L, "GlobalCyclingNetwork" );
        List<sihoiba.interviewHomework.model.Channel> wantedChannels = new ArrayList<>( Arrays.asList( wantedInternalChannel1, wantedInternalChannel2 ) );

        ChannelListResponse channelListResponse = new ChannelListResponse();
        Channel wantedChannel1 = getChannel( "globalmtb", "globalmtb_id" );
        Channel wantedChannel2 = getChannel(  "GlobalCyclingNetwork","GlobalCyclingNetwork_id" );
        Channel unwantedChannel = getChannel( "unwanted","unwanted_id" );
        List<Channel> channelsList = new ArrayList<>( Arrays.asList( wantedChannel1, wantedChannel2, unwantedChannel ) );
        channelListResponse.setItems( channelsList );

        given( mockYouTube.channels() ).willReturn( mockChannels );
        given( mockChannels.list( "id, snippet" ) ).willReturn( mockChannelsList );
        given( mockChannelsList.setKey( API_KEY ) ).willReturn( mockChannelsList );
        given( mockChannelsList.execute() ).willReturn( channelListResponse );
        given( mockChannelsDao.getAllChannels() ).willReturn( wantedChannels );
    }

    private SearchResult getSearchResult( String title ) {
        SearchResult searchResult = new SearchResult();
        SearchResultSnippet searchResultSnippet = new SearchResultSnippet();
        searchResultSnippet.setTitle( title );
        searchResult.setSnippet( searchResultSnippet );
        return searchResult;
    }

    private Channel getChannel( String title, String id ) {
        Channel channel = new Channel();
        ChannelSnippet channelSnippet = new ChannelSnippet();
        channelSnippet.setTitle( title );
        channel.setId( id );
        channel.setSnippet( channelSnippet );
        return channel;
    }
}
