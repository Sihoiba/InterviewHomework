package sihoiba.interviewHomework.client;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import sihoiba.interviewHomework.exception.ApplicationException;
import sihoiba.interviewHomework.persistence.ChannelsRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Client for the Youtube v3 API.
 */
@Component
public class YouTubeClient {

    private static final Logger LOG = LoggerFactory.getLogger( YouTubeClient.class );

    //Obtained manually from the URL on the Youtube website, it's not possible to obtain this using the API Key as it requires auth
    private static final String GLOBAL_CYCLING_NETWORK_CHANNEL_ID = "UCuTaETsuCOkJ0H_GAztWt0Q";

    //Obtained manually from the URL on the Youtube website, it's not possible to obtain this using the API Key as it requires auth
    private static final String GLOBALMTB_CHANNEL_ID = "UC_A--fhX5gea0i4UtpD99Gg";

    //This is the maximum number of search results per page Youtube allows.
    private static final Long MAX_SEARCH_RESULTS = 50L;

    @Autowired
    private ChannelsRepository channelsRepository;

    @Autowired
    private YouTube youTube;

    @Value( "${interview-homework.youtube.api.token}" )
    private String apiKey;

    private static final List<String> searchFilterList;

    static {
        try ( InputStream resource = new ClassPathResource( "search_filter" ).getInputStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( resource ) ) ) {
            searchFilterList = reader.lines().collect( Collectors.toList() );
        } catch ( IOException e ) {
            LOG.error( "Cannot create Youtube client: {}", e.getMessage() );
            throw new ApplicationException( "Failed to read search_filter", e );
        }
    }

    /**
     * Get video details from youTube as per the search filter.
     *
     * @return A list of the retrieved search result snippets.
     */
    public List<SearchResultSnippet> getVideoDetails() {
        List<SearchResultSnippet> videoSnippets = new ArrayList<>();
        try {

            // Ideally get channel ids using this method List<String> wantedChannelIds = getChannelIds();
            List<String> wantedChannelIds = new ArrayList<>( Arrays.asList( GLOBAL_CYCLING_NETWORK_CHANNEL_ID, GLOBALMTB_CHANNEL_ID ) );
            String nextPageToken = null;
            for ( String channelId : wantedChannelIds ) {
                do {
                    YouTube.Search.List searchListByKeywordRequest = youTube.search().list( "snippet" ).setKey( apiKey );
                    searchListByKeywordRequest.setChannelId( channelId );
                    searchListByKeywordRequest.setMaxResults( MAX_SEARCH_RESULTS );
                    if ( nextPageToken != null ) {
                        searchListByKeywordRequest.setPageToken( nextPageToken );
                    }
                    SearchListResponse response = searchListByKeywordRequest.execute();
                    List<SearchResult> results = response.getItems();
                    if ( results != null ) {
                        extractWantedVideoSnippets( videoSnippets, results );
                    }
                    nextPageToken = response.getNextPageToken();
                } while ( nextPageToken != null ); //Google returns null next page token when you have retrieved the last page.
            }

        } catch ( IOException e ) {
            throw new ApplicationException( "Failed to get video details from Youtube", e );
        }
        return videoSnippets;
    }

    /**
     * Only include video snippets whose title includes one of the wanted filter words ignoring case
     *
     * @param videoSnippetsToAddTo the list to add the matching VideoSnippet to
     * @param results              the search results to filter
     */
    private void extractWantedVideoSnippets( List<SearchResultSnippet> videoSnippetsToAddTo, List<SearchResult> results ) {
        for ( SearchResult result : results ) {
            for ( String wantedTitle : searchFilterList ) {
                String formattedWantedTitle = wantedTitle.toLowerCase();
                String formattedSnippetTitle = result.getSnippet().getTitle().toLowerCase();
                if ( formattedSnippetTitle.contains( formattedWantedTitle ) ) {
                    videoSnippetsToAddTo.add( result.getSnippet() );
                }
            }
        }
    }

    /**
     * If we were doing authorised requests, we could use this method to get the channel ids.
     * However we only have an API key which is insufficient
     *
     * @return List of Channel Ids
     * @throws IOException thrown by the youtube api layer.
     */
    private List<String> getChannelIds() throws IOException {
        ChannelListResponse channelListResponse = youTube.channels().list( "id, snippet" ).setMine( true ).setKey( apiKey ).execute();
        List<Channel> channels = channelListResponse.getItems();
        List<String> wantedChannelTitle = getWantedChannelTitles();
        List<String> wantedChannelIds = new ArrayList<>();
        for ( Channel channel : channels ) {
            if ( wantedChannelTitle.contains( channel.getSnippet().getTitle() ) ) {
                wantedChannelIds.add( channel.getId() );
            }
        }
        return wantedChannelIds;
    }

    private List<String> getWantedChannelTitles() {
        List<sihoiba.interviewHomework.model.Channel> wantedChannels = channelsRepository.findAll();
        List<String> wantedChannelTitles = new ArrayList<>();
        for ( sihoiba.interviewHomework.model.Channel wantedChannel : wantedChannels ) {
            wantedChannelTitles.add( wantedChannel.getChannelName() );
        }
        return wantedChannelTitles;
    }

    public void setApiKey( String apiKey ) {
        this.apiKey = apiKey;
    }
}
