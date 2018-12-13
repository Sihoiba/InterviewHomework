package sihoiba.interviewHomework.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sihoiba.interviewHomework.persistence.VideosDao;

@RunWith( MockitoJUnitRunner.class )
public class YoutubeVideoDetailsServiceTest {

	@Mock
	VideosDao mockVideosDao;

	@InjectMocks
	YoutubeVideoDetailsService youtubeVideoDetailsService;

	@Test
	public void shouldPopulateVideoDetails() {

	}

	@Test
	public void shouldGetAllStoredVideoDetails() {

	}

	@Test
	public void shouldGetVideoDetails() {

	}

	@Test
	public void shouldDeleteVideoDetails() {

	}

}
