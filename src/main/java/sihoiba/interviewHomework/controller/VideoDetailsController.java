package sihoiba.interviewHomework.controller;

import sihoiba.interviewHomework.model.Video;
import sihoiba.interviewHomework.service.YoutubeVideoDetailsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller that provides all methods for storing video data from Youtube, retrieving and searching it.
 */
@Controller
public class VideoDetailsController {

    @Autowired
    private YoutubeVideoDetailsService youtubeVideoDetailsService;

    @PostMapping
    public void populateVideoDetails() {
    }

    @GetMapping
    public List<Video> getAllVideoDetails() {
        return null;
    }

    @GetMapping
    public Video getVideoDetailsById( Long id ) {
        return null;
    }

    @DeleteMapping
    public void deleteVideoDetails( Long id ) {
    }
}
