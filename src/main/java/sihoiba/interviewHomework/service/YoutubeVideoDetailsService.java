package sihoiba.interviewHomework.service;

import com.google.api.services.youtube.model.SearchResultSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sihoiba.interviewHomework.client.YouTubeClient;
import sihoiba.interviewHomework.model.SearchTerm;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.model.VideoDetailsSearchResult;
import sihoiba.interviewHomework.persistence.VideosDao;
import sihoiba.interviewHomework.util.GoogleDateTimeConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for retrieving, storing and deleting video details.
 */
@Component
public class YoutubeVideoDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger( YoutubeVideoDetailsService.class );

    @Autowired
    private YouTubeClient youtubeClient;

    @Autowired
    private VideosDao videosDao;

    public void populateVideoDetails() {
        List<SearchResultSnippet> videoSnippets = youtubeClient.getVideoDetails();
        for ( SearchResultSnippet videoSnippet : videoSnippets ) {
            LOG.info( "Storing details for video with title {}", videoSnippet.getTitle() );
            Video video = new Video( videoSnippet.getTitle(), GoogleDateTimeConverter.getDateTime( videoSnippet.getPublishedAt() ) );
            videosDao.create( video );
        }
    }

    public List<Video> getAllStoredVideoDetails() {
        return videosDao.getAllVideos();
    }

    public Video getVideoDetails( Long id ) {
        return videosDao.get( id );
    }

    public void deleteVideoDetails( Long id ) {
        videosDao.delete( id );
    }

    public List<VideoDetailsSearchResult> searchVideos( SearchTerm searchTerm ) {
        List<Video> matchingVideos = new ArrayList<>();
        switch ( searchTerm.getSearchField() ) {
            case TITLE:
                matchingVideos = videosDao.findVideosWithMatchingTitle( searchTerm.getValueToMatch() );
                break;
            default:
                return new ArrayList<>();
        }

        if ( !matchingVideos.isEmpty() ) {
            List<VideoDetailsSearchResult> results = new ArrayList<>();
            for( Video matchingVideo : matchingVideos ) {
                VideoDetailsSearchResult matchingResult = new VideoDetailsSearchResult( matchingVideo.getId(), matchingVideo.getTitle() );
                results.add( matchingResult );
            }
            return results;
        } else {
            return new ArrayList<>();
        }
    }
}
