package sihoiba.interviewHomework.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import sihoiba.interviewHomework.model.Channel;

import java.sql.Types;
import java.util.List;

/**
 * DAO for interacting with the channels database table.
 */
@Repository
public class ChannelsDao {

    private static final Logger LOG = LoggerFactory.getLogger( ChannelsDao.class );

    private JdbcTemplate jdbcTemplate;

    public ChannelsDao( JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create the given {@link sihoiba.interviewHomework.model.Channel}
     * @param channel the channel to create
     */
    public void create( Channel channel ) {
        Assert.notNull( channel, "channel must not be null" );

        String query = "INSERT INTO mydb.channels (id, channel_name) values (?, ?)";

        Object[] params = new Object[] {
                channel.getId(),
                channel.getChannelName()
        };

        int[] paramTypes = new int[] {
                Types.BIGINT,
                Types.VARCHAR
        };

        LOG.info( "Creating channel in database" );
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory( query, paramTypes );
        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory.newPreparedStatementCreator( params );

        jdbcTemplate.update( preparedStatementCreator );
    }

    /**
     * Get all channels stored
     * @return the stored channels
     */
    public List<Channel> getAllChannels() {
        String selectQuery = "SELECT id, channel_name FROM mydb.channels";
        Object[] selectParams = new Object[] {};
        List<Channel> records = jdbcTemplate.query( selectQuery, selectParams, new ChannelsRowMapper() );

        if ( records.isEmpty() ) {
            LOG.info( "No channels found" );
        }
        return records;
    }
}
