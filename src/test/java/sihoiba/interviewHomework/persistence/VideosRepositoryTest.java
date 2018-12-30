package sihoiba.interviewHomework.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    private TestEntityManager entityManager;

    @Autowired
    private VideosRepository classUnderTest;

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
        Video created = entityManager.persist( new Video( title, now ) );

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
        String title1 = "someTitle1";
        LocalDateTime now1 = LocalDateTime.now().withNano( 0 );
        Video video1 = entityManager.persist( new Video( title1, now1 ) );

        String title2 = "someTitle2";
        LocalDateTime now2 = LocalDateTime.now().withNano( 0 );
        Video video2 = entityManager.persist( new Video( title2, now2 ) );

        // When
        List<Video> result = classUnderTest.findAll();

        // Then
        assertThat( result ).hasSize( 2 );
        assertThat( result ).containsExactly( video1, video2 );
    }

    @Test
    public void shouldFindVideosWithMatchingTitles() {
        // Given
        String searchTerm = "cycling";
        String title1 = "someTitle" + searchTerm.toUpperCase();
        LocalDateTime now1 = LocalDateTime.now().withNano( 0 );
        Video video1 = entityManager.persist( new Video( title1, now1 ) );

        String title2 = "someTitle";
        LocalDateTime now2 = LocalDateTime.now().withNano( 0 );
        entityManager.persist( new Video( title2, now2 ) );

        // When
        List<Video> result = classUnderTest.findByTitleContainingIgnoreCase( searchTerm );

        // Then
        assertThat( result ).hasSize( 1 );
        assertThat( result ).containsExactly( video1 );
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
        Video video = entityManager.persist( new Video( title, now ) );

        // When
        classUnderTest.deleteById( video.getId() );

        // Then
        Video deleted = entityManager.find( Video.class, video.getId() );
        assertThat( deleted ).isNull();

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
