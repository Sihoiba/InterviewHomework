package sihoiba.interviewHomework.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import sihoiba.interviewHomework.model.Channel;
import sihoiba.interviewHomework.model.Video;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * Test for {@link ChannelsRepository}
 */
@RunWith( SpringRunner.class)
@DataJpaTest
public class ChannelsRepositoryTest {

    @Autowired
    ChannelsRepository classUnderTest;

    @Test
    public void shouldSave() {
        // Given
        String channelName = "someChannel";
        Channel channel = new Channel( channelName );

        // When
        Channel result = classUnderTest.save( channel );

        //Then
        assertThat( result ).isNotNull();
        assertThat( result ).hasNoNullFieldsOrProperties().hasFieldOrPropertyWithValue( "channelName", channelName );

    }

    @Test
    public void shouldFailToSaveChannelGivenNullChannel() {
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
    public void shouldFindAllVideos() {
        // Given
        String channelName = "someChannel";
        Channel channel1 = classUnderTest.save( new Channel( channelName ) );

        String title2 = "someOtherChannel";
        Channel channel2 = classUnderTest.save( new Channel( channelName ) );

        // When
        List<Channel> result = classUnderTest.findAll();

        // Then
        assertThat( result ).hasSize( 2 );
        assertThat( result ).containsExactly( channel1, channel2 );
    }
}
