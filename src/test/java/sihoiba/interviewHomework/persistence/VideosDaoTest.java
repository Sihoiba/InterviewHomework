package sihoiba.interviewHomework.persistence;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sihoiba.interviewHomework.model.Video;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * Test for {@link sihoiba.interviewHomework.persistence.VideosDao}
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = DaoTestConfiguration.class )
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class VideosDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger( VideosDaoTest.class );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    VideosDao videosDao;

    @After
    public void tearDown() {
        String query = "TRUNCATE TABLE mydb.videos";
        jdbcTemplate.update( query );
    }

    @Test
    public void shouldCreateVideo() {
        // Given
        Long id = 1L;
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        Video video = new Video( id, title, now );

        // When
        videosDao.create( video );

        // Then
        getVideo( id );
        assertThat( video ).isNotNull();
        assertThat( video ).hasFieldOrPropertyWithValue( "id", id )
                .hasFieldOrPropertyWithValue( "title", title )
                .hasFieldOrPropertyWithValue( "publishedAt", now );
    }

    @Test
    public void shouldFailToCreateVideoGivenNullVideo() {
        // Given

        // When
        try {
            videosDao.create( null );
            shouldHaveThrown( IllegalArgumentException.class );
            // Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "video must not be null" );
        }
    }

    @Test
    public void shouldGetVideoById() {
        // Given
        Long id = 1L;
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        createVideo( id, title, now );

        // When
        Video result = videosDao.get( id );

        // Then
        assertThat( result ).isNotNull();
        assertThat( result ).hasFieldOrPropertyWithValue( "id", id )
                .hasFieldOrPropertyWithValue( "title", title )
                .hasFieldOrPropertyWithValue( "publishedAt", now );

    }

    @Test
    public void shouldNotGetVideoByIdGivenVideoDoesNotExist() {
        // Given
        Long id = 1L;

        // When
        Video result = videosDao.get( id );

        // Then
        assertThat( result ).isNull();
    }

    @Test
    public void shouldFailToGetVideoByIdGivenNullId() {
        // Given

        // When
        try {
            videosDao.get( null );
            shouldHaveThrown( IllegalArgumentException.class );
            // Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "id must not be null" );
        }
    }

    @Test
    public void shouldAllGetVideos() {
        // Given
        Long id1 = 1L;
        String title1 = "someTitle";
        LocalDateTime now1 = LocalDateTime.now();
        createVideo( id1, title1, now1 );

        Long id2 = 2L;
        String title2 = "someTitle";
        LocalDateTime now2 = LocalDateTime.now();
        createVideo( id2, title2, now2 );

        // When
        List<Video> result = videosDao.getAllVideos();

        // Then
        assertThat( result ).hasSize( 2 );
        assertThat( result.get( 0 ) ).hasFieldOrPropertyWithValue( "id", id1 )
                .hasFieldOrPropertyWithValue( "title", title1 )
                .hasFieldOrPropertyWithValue( "publishedAt", now1 );
        assertThat( result.get( 1 ) ).hasFieldOrPropertyWithValue( "id", id2 )
                .hasFieldOrPropertyWithValue( "title", title2 )
                .hasFieldOrPropertyWithValue( "publishedAt", now2 );
    }

    @Test
    public void shouldDeleteVideo() {
        // Given
        Long id = 1L;
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        createVideo( id, title, now );

        // When
        videosDao.delete( id );

        // Then
        Video video = getVideo( id );
        assertThat( video ).isNull();

    }

    @Test
    public void shouldFailToDeleteVideoGivenNullId() {
        // Given

        // When
        try {
            videosDao.delete( null );
            shouldHaveThrown( IllegalArgumentException.class );
            // Then
        } catch ( IllegalArgumentException expected ) {
            assertThat( expected ).hasMessage( "id must not be null" );
        }
    }

    private Video getVideo( Long id ) {
        String query = "SELECT id, title, date FROM mydb.videos WHERE id = ?";
        Object[] selectParams = new Object[] { id };
        List<Video> videos = jdbcTemplate.query( query, selectParams, new VideosRowMapper() );
        if ( videos.size() == 1 ) {
            return videos.get( 0 );
        } else if ( videos.size() > 1 ) {
            throw new IllegalStateException( "id must be unique" );
        } else {
            return null;
        }
    }

    private void createVideo( Long id, String title, LocalDateTime datetime ) {
        String query = "INSERT INTO mydb.videos(id, title, date) values (?, ?, ?)";
        Object[] params = new Object[] {
          id,
          title,
          datetime
        };

        jdbcTemplate.update( query, params );
        try {
            Thread.sleep( 10 );
        } catch ( Exception ex ) {
            LOG.error( "Sleep interrupted {}", ex.getMessage() );
        }
    }
}
