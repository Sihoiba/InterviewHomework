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
import sihoiba.interviewHomework.model.Channel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * Test for {@link ChannelsDao}
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = DaoTestConfiguration.class )
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class ChannelsDaoTest {

	private static final Logger LOG = LoggerFactory.getLogger( VideosDaoTest.class );

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	ChannelsDao channelsDao;

	@After
	public void tearDown() {
		String query = "TRUNCATE TABLE mydb.channels";
		jdbcTemplate.update( query );
	}

	@Test
	public void shouldCreateChannel() {
		// Given
		Long id = 1L;
		String channelName = "testChannel";
		Channel channel = new Channel( id, channelName );

		// When
		channelsDao.create( channel );

		// Then
		getChannel( id );
		assertThat( channel ).isNotNull();
		assertThat( channel ).hasFieldOrPropertyWithValue( "id", id )
				.hasFieldOrPropertyWithValue( "channelName", channelName );
	}

	@Test
	public void shouldFailToCreateChannelGivenNullChannel() {
		// Given

		// When
		try {
			channelsDao.create( null );
			shouldHaveThrown( IllegalArgumentException.class );
			// Then
		} catch ( IllegalArgumentException expected ) {
			assertThat( expected ).hasMessage( "channel must not be null" );
		}
	}

	@Test
	public void shouldGetAllChannels() {
		// Given
		Long id1 = 1L;
		String channelName1 = "testChannel1";
		createChannel( id1, channelName1 );

		Long id2 = 2L;
		String channelName2 = "testChannel2";
		createChannel( id2, channelName2 );

		// When
		List<Channel> result = channelsDao.getAllChannels();

		// Then
		assertThat( result ).hasSize( 2 );
		assertThat( result.get( 0 ) ).hasFieldOrPropertyWithValue( "id", id1 )
				.hasFieldOrPropertyWithValue( "channelName", channelName1 );
		assertThat( result.get( 1 ) ).hasFieldOrPropertyWithValue( "id", id2 )
				.hasFieldOrPropertyWithValue( "channelName", channelName2 );
	}

	private Channel getChannel( Long id ) {
		String query = "SELECT id, channel_name FROM mydb.channels WHERE id = ?";
		Object[] selectParams = new Object[] { id };
		List<Channel> channels = jdbcTemplate.query( query, selectParams, new ChannelsRowMapper() );
		if ( channels.size() == 1 ) {
			return channels.get( 0 );
		} else if ( channels.size() > 1 ) {
			throw new IllegalStateException( "id must be unique" );
		} else {
			return null;
		}
	}

	private void createChannel( Long id, String title ) {
		String query = "INSERT INTO mydb.channels(id, channel_name) values (?, ?)";
		Object[] params = new Object[] {
				id,
				title
		};

		jdbcTemplate.update( query, params );
		try {
			Thread.sleep( 10 );
		} catch ( Exception ex ) {
			LOG.error( "Sleep interrupted {}", ex.getMessage() );
		}
	}

}
