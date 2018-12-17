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
import org.springframework.stereotype.Component;
import sihoiba.interviewHomework.exception.ApplicationException;
import sihoiba.interviewHomework.persistence.ChannelsDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for the Youtube v3 API.
 */
@Component
public class YouTubeClient {

    private static final Logger LOG = LoggerFactory.getLogger( YouTubeClient.class );

    @Autowired
    private ChannelsDao channelsDao;

    @Autowired
    private YouTube youTube;

    @Value( "interview-homework.youtube.api.token" )
    private String apiKey;

    private static final List<String> searchFilterList;
    static {
        try {
            File searchFilter = new File( YouTubeClient.class.getResource( "/search_filter" ).getPath() );
            searchFilterList = Files.readAllLines( searchFilter.toPath() );
        } catch ( IOException e ) {
            LOG.error( "Cannot create Youtube client: {}", e.getMessage() );
            throw new ApplicationException( "Failed to read search_filter", e );
        }
    }

    /**
     * Get video details from youTube as per the search filter.
     * @return
     */
    public List<SearchResultSnippet> getVideoDetails() {
        List<SearchResultSnippet> videoSnippets = new ArrayList<>();
        try {

            ChannelListResponse channelListResponse = youTube.channels().list("id, snippet" ).setOauthToken( apiKey ).execute();
            List<Channel> channels = channelListResponse.getItems();
            List<String> wantedChannelTitle = getWantedChannelTitles();
            List<String> wantedChannelIds = new ArrayList<>();
            for ( Channel channel : channels ) {
                if ( wantedChannelTitle.contains( channel.getSnippet().getTitle() ) ) {
                    wantedChannelIds.add( channel.getId() );
                }
            }
            for ( String channelId : wantedChannelIds ) {
                YouTube.Search.List searchListByKeywordRequest = youTube.search().list( "snippet" ).setOauthToken( apiKey );
                searchListByKeywordRequest.setChannelId( channelId );
                SearchListResponse response = searchListByKeywordRequest.execute();
                List<SearchResult> results = response.getItems();
                for ( SearchResult result : results ) {
                    for ( String wantedTitle : searchFilterList ) {
                        if ( wantedTitle.equalsIgnoreCase( result.getSnippet().getTitle() ) ) {
                            videoSnippets.add( result.getSnippet() );
                        }
                    }
                }
            }

        } catch( IOException e ) {
            throw new ApplicationException( "Failed to get video details from Youtube", e );
        }
        return videoSnippets;
    }

    private List<String> getWantedChannelTitles() {
        List<sihoiba.interviewHomework.model.Channel> wantedChannels = channelsDao.getAllChannels();
        List<String> wantedChannelTitles = new ArrayList<>();
        for( sihoiba.interviewHomework.model.Channel wantedChannel: wantedChannels ) {
            wantedChannelTitles.add( wantedChannel.getChannelName() );
        }
        return wantedChannelTitles;
    }

    public void setApiKey( String apiKey ) {
        this.apiKey = apiKey;
    }
}
