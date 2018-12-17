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

import sihoiba.interviewHomework.persistence.ChannelsDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * Unit test for {@link YouTubeClient}
 */
@RunWith( MockitoJUnitRunner.class )
public class YouTubeClientTest {

    @Mock
    private ChannelsDao mockChannelsDao;

    @Mock
    private YouTube mockYouTube;

    @InjectMocks
    private YouTubeClient classUnderTest;

    private static final String API_KEY = "api_key";

    @Before
    public void setup() {
        classUnderTest.setApiKey( API_KEY );
    }

    @Test
    public void shouldGetVideoDetails() throws Exception {
        // Given
        YouTube.Channels mockChannels = mock( YouTube.Channels.class );
        YouTube.Channels.List mockChannelsList = mock( YouTube.Channels.List.class );
        YouTube.Search mockSearch = mock( YouTube.Search.class );
        YouTube.Search.List mockSearchList = mock( YouTube.Search.List.class );

        ChannelListResponse channelListResponse = new ChannelListResponse();
        Channel wantedChannel1 = getChannel( "globalmtb", "globalmtb_id" );
        Channel wantedChannel2 = getChannel(  "GlobalCyclingNetwork","GlobalCyclingNetwork_id" );
        Channel unwantedChannel = getChannel( "unwanted","unwanted_id" );
        List<Channel> channelsList = new ArrayList<>( Arrays.asList( wantedChannel1, wantedChannel2, unwantedChannel ) );
        channelListResponse.setItems( channelsList );

        sihoiba.interviewHomework.model.Channel wantedInternalChannel1 = new sihoiba.interviewHomework.model.Channel( 1L, "globalmtb" );
        sihoiba.interviewHomework.model.Channel wantedInternalChannel2 = new sihoiba.interviewHomework.model.Channel( 1L, "GlobalCyclingNetwork" );
        List<sihoiba.interviewHomework.model.Channel> wantedChannels = new ArrayList<>( Arrays.asList( wantedInternalChannel1, wantedInternalChannel2 ) );

        SearchListResponse searchListResponse1 = new SearchListResponse();
        SearchResult searchResult1 = getSearchResult( "PRO" );
        SearchResult searchResult2 = getSearchResult( "Not wanted" );
        List<SearchResult> searchResults1 = new ArrayList<>( Arrays.asList( searchResult1, searchResult2 ) );
        searchListResponse1.setItems( searchResults1 );
        SearchListResponse searchListResponse2 = new SearchListResponse();
        SearchResult searchResult3 = getSearchResult( "matt stephens" );
        List<SearchResult> searchResults2 = new ArrayList<>( Arrays.asList( searchResult3 ));
        searchListResponse2.setItems( searchResults2 );

        given( mockYouTube.channels() ).willReturn( mockChannels );
        given( mockChannels.list( "id, snippet" ) ).willReturn( mockChannelsList );
        given( mockChannelsList.setOauthToken( API_KEY ) ).willReturn( mockChannelsList );
        given( mockChannelsList.execute() ).willReturn( channelListResponse );
        given( mockChannelsDao.getAllChannels() ).willReturn( wantedChannels );
        given( mockYouTube.search() ).willReturn( mockSearch ).willReturn( mockSearch );
        given( mockSearch.list( "snippet" ) ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.setOauthToken( API_KEY ) ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.setChannelId( anyString() ) ).willReturn( mockSearchList ).willReturn( mockSearchList );
        given( mockSearchList.execute() ).willReturn( searchListResponse1 ).willReturn( searchListResponse2 );

        // When
        List<SearchResultSnippet> results = classUnderTest.getVideoDetails();

        // Then
        assertThat( results ).hasSize( 2 );
        assertThat( results.get( 0 ) ).hasFieldOrPropertyWithValue( "title", "PRO" );
        assertThat( results.get( 1 ) ).hasFieldOrPropertyWithValue( "title", "matt stephens" );
        then( mockYouTube ).should().channels();
        then( mockChannels ).should().list( "id, snippet" );
        then( mockChannelsList ).should().setOauthToken( API_KEY );
        then( mockChannelsList ).should().execute();
        then( mockChannelsDao ).should().getAllChannels();
        then( mockYouTube ).should( times( 2 ) ).search();
        then( mockSearch ).should( times( 2 ) ).list( "snippet" );
        then( mockSearchList ).should( times( 2 ) ).setOauthToken( API_KEY );
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass( String.class );
        then( mockSearchList ).should( times( 2 ) ).setChannelId( stringArgumentCaptor.capture() );
        assertThat( stringArgumentCaptor.getAllValues() ).containsExactlyInAnyOrder( "globalmtb_id", "GlobalCyclingNetwork_id" );
        then( mockSearchList ).should( times( 2 ) ).execute();
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

    @Test
    public void shouldFailToGetVideoDetailsGivenProblemGettingChannels() {

    }

    @Test
    public void shouldFailToGetVideoDetailsGivenProblemSearchingVideos() {

    }
}
