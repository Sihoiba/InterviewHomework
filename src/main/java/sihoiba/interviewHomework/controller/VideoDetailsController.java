package sihoiba.interviewHomework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sihoiba.interviewHomework.exception.EntityNotFoundException;
import sihoiba.interviewHomework.model.SearchTermType;
import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.model.VideoDetailsSearchResponse;
import sihoiba.interviewHomework.service.YoutubeVideoDetailsService;

import java.util.List;

/**
 * Controller that provides all methods for storing video data from Youtube, retrieving and searching it.
 */
@RestController
public class VideoDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger( VideoDetailsController.class );

    @Autowired
    private YoutubeVideoDetailsService youtubeVideoDetailsService;

    @PostMapping( path="/populateVideos" )
    public void populateVideoDetails() {
        LOG.info( "Populating stored video details" );
        youtubeVideoDetailsService.populateVideoDetails();
    }

    @GetMapping( path="/videos", produces = { MediaType.APPLICATION_JSON_VALUE } )
    public List<Video> getAllVideoDetails() {
        LOG.info( "Getting all stored video details." );
        return youtubeVideoDetailsService.getAllStoredVideoDetails();
    }

    @GetMapping( path="/videos/{id}", produces = { MediaType.APPLICATION_JSON_VALUE } )
    public Video getVideoDetailsById( @PathVariable Long id ) throws EntityNotFoundException {
        Assert.notNull( id, "id must not be null." );
        LOG.info( "Getting stored video details with {}", id );
        return youtubeVideoDetailsService.getVideoDetails( id );
    }

    @DeleteMapping( path="/videos/{id}" )
    public void deleteVideoDetails( @PathVariable Long id ) {
        Assert.notNull( id, "id must not be null." );
        LOG.info( "Deleting stored video details with {}", id );
        youtubeVideoDetailsService.deleteVideoDetails( id );
    }

    @GetMapping( path="/searchVideos", produces = { MediaType.APPLICATION_JSON_VALUE } )
    public VideoDetailsSearchResponse searchVideoDetails( @RequestParam( "searchTermType" ) SearchTermType searchTermType, @RequestParam( "value" ) String value ) {
        Assert.notNull( searchTermType, "searchTermType must not be null." );
        Assert.notNull( value, "value must not be null." );
        LOG.info( "Searching by {} on value {}", searchTermType, value );
        return new VideoDetailsSearchResponse( youtubeVideoDetailsService.searchVideos( searchTermType, value ) );
    }
}
