package sihoiba.interviewHomework.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import sihoiba.interviewHomework.model.Video;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * Test for {@link VideosRepository}
 */
@RunWith( SpringRunner.class)
@DataJpaTest
public class VideosRepositoryTest {

    @Autowired
    VideosRepository classUnderTest;

    @Test
    public void shouldSave() {
        // Given
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        Video video = new Video( title, now );

        // When
        Video result = classUnderTest.save( video );

        //Then
        assertThat( result ).isNotNull();
        assertThat( result ).hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue( "title", title ).hasFieldOrPropertyWithValue( "publishedAt", now );

    }

    @Test
    public void shouldFailToSaveVideoGivenNullVideo() {
        // Given

        // When
        try {
            classUnderTest.save( null );
            shouldHaveThrown( InvalidDataAccessApiUsageException.class );
            // Then
        } catch ( InvalidDataAccessApiUsageException expected ) {
            assertThat( expected ).hasMessage( "Target object must not be null; nested exception is java.lang.IllegalArgumentException: Target object must not be null" );
        }
    }

    @Test
    public void shouldFindById() {
        // Given
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        Video created = classUnderTest.save( new Video( title, now ) );

        // When
        Optional<Video> result = classUnderTest.findById( created.getId() );

        // Then
        assertThat( result.isPresent() ).isTrue();
        assertThat( result.get() ).hasFieldOrPropertyWithValue( "id", created.getId() )
                .hasFieldOrPropertyWithValue( "title", title ).hasFieldOrPropertyWithValue( "publishedAt", now );
    }

    @Test
    public void shouldNotFindByIdGivenVideoDoesNotExist() {
        // Given
        Long id = 1L;

        // When
        Optional<Video> result = classUnderTest.findById( id );

        // Then
        assertThat( result.isPresent() ).isFalse();
    }

    @Test
    public void shouldFailToFindByIdGivenNullId() {
        // Given

        // When
        try {
            classUnderTest.findById( null );
            shouldHaveThrown( InvalidDataAccessApiUsageException.class );
            // Then
        } catch ( InvalidDataAccessApiUsageException expected ) {
            assertThat( expected ).hasMessage( "The given id must not be null!; nested exception is java.lang.IllegalArgumentException: The given id must not be null!" );
        }
    }

    @Test
    public void shouldFindAllVideos() {
        // Given
        String title1 = "someTitle";
        LocalDateTime now1 = LocalDateTime.now().withNano( 0 );
        Video video1 = classUnderTest.save( new Video( title1, now1 ) );

        String title2 = "someTitle";
        LocalDateTime now2 = LocalDateTime.now().withNano( 0 );
        Video video2 = classUnderTest.save( new Video( title2, now2 ) );

        // When
        List<Video> result = classUnderTest.findAll();

        // Then
        assertThat( result ).hasSize( 2 );
        assertThat( result.get( 0 ) ).hasFieldOrPropertyWithValue( "id", video1.getId() )
                .hasFieldOrPropertyWithValue( "title", title1 )
                .hasFieldOrPropertyWithValue( "publishedAt", now1 );
        assertThat( result.get( 1 ) ).hasFieldOrPropertyWithValue( "id", video2.getId() )
                .hasFieldOrPropertyWithValue( "title", title2 )
                .hasFieldOrPropertyWithValue( "publishedAt", now2 );
    }

    @Test
    public void shouldFindVideosWithMatchingTitles() {
        // Given
        String searchTerm = "cycling";
        String title1 = "someTitle" + searchTerm.toUpperCase();
        LocalDateTime now1 = LocalDateTime.now().withNano( 0 );
        Video video1 = classUnderTest.save( new Video( title1, now1 ) );

        String title2 = "someTitle";
        LocalDateTime now2 = LocalDateTime.now().withNano( 0 );
        classUnderTest.save( new Video( title2, now2 ) );

        // When
        List<Video> result = classUnderTest.findByTitleContainingIgnoreCase( searchTerm );

        // Then
        assertThat( result ).hasSize( 1 );
        assertThat( result.get( 0 ) ).hasFieldOrPropertyWithValue( "id", video1.getId() )
                .hasFieldOrPropertyWithValue( "title", title1 )
                .hasFieldOrPropertyWithValue( "publishedAt", now1 );
    }

    @Test
    public void shouldFailToFindVideosWithMatchingTitlesGivenNullValueToMatch() {
        // Given

        // When
        try {
            classUnderTest.findByTitleContainingIgnoreCase( null );
            shouldHaveThrown( InvalidDataAccessApiUsageException.class );
            // Then
        } catch ( InvalidDataAccessApiUsageException expected ) {
            assertThat( expected ).hasMessage( "Value must not be null!; nested exception is java.lang.IllegalArgumentException: Value must not be null!" );
        }
    }

    @Test
    public void shouldDeleteVideo() {
        // Given
        String title = "someTitle";
        LocalDateTime now = LocalDateTime.now();
        Video video = classUnderTest.save( new Video( title, now ) );
        Optional<Video> created = classUnderTest.findById( video.getId() );
        assertThat( created.isPresent() ).isTrue();

        // When
        classUnderTest.deleteById( video.getId() );

        // Then
        Optional<Video> deleted = classUnderTest.findById( video.getId() );
        assertThat( deleted.isPresent() ).isFalse();

    }

    @Test
    public void shouldFailToDeleteVideoGivenNullId() {
        // Given

        // When
        try {
            classUnderTest.deleteById( null );
            shouldHaveThrown( InvalidDataAccessApiUsageException.class );
            // Then
        } catch ( InvalidDataAccessApiUsageException expected ) {
            assertThat( expected ).hasMessage( "The given id must not be null!; nested exception is java.lang.IllegalArgumentException: The given id must not be null!" );
        }
    }

}
