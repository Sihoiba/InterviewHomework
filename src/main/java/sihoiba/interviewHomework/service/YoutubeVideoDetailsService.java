package sihoiba.interviewHomework.service;

import sihoiba.interviewHomework.client.YoutubeClient;
import sihoiba.interviewHomework.model.Video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class YoutubeVideoDetailsService {

    @Autowired
    private YoutubeClient youtubeClient;

    public void populateVideoDetails() {
    }

    public List<Video> getAllStoredVideoDetails() {
        return null;
    }

    public Video getVideoDetails( Long id ) {
        return null;
    }

    public void deleteVideoDetails( Long id ) {
    }
}
