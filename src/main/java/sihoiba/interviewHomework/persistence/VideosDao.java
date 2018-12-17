package sihoiba.interviewHomework.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.util.Assert;
import sihoiba.interviewHomework.model.Video;

import java.sql.Types;
import java.util.List;

/**
 * DAO for interacting with the videos database table.
 */
public class VideosDao {

    private static final Logger LOG = LoggerFactory.getLogger( VideosDao.class );

    private JdbcTemplate jdbcTemplate;

    public VideosDao( JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create the given {@link sihoiba.interviewHomework.model.Video}
     * @param video the video to create
     */
    public void create( Video video ) {
        Assert.notNull( video, "video must not be null" );

        String query = "INSERT INTO mydb.videos (id, title, date) values (?, ?, ?)";

        Object[] params = new Object[] {
                video.getId(),
                video.getTitle(),
                video.getPublishedAt()
        };

        int[] paramTypes = new int[] {
                Types.BIGINT,
                Types.VARCHAR,
                Types.TIMESTAMP
        };

        LOG.info( "Creating video in database" );
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory( query, paramTypes );
        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory.newPreparedStatementCreator( params );

        jdbcTemplate.update( preparedStatementCreator );
    }

    /**
     * Get the {@link sihoiba.interviewHomework.model.Video} with the corresponding id
     * @param id the video id
     * @return the stored video matching the id
     */
    public Video get( Long id ) {
        Assert.notNull( id, "id must not be null" );
        String selectQuery = "SELECT id, title, date FROM mydb.videos WHERE id = ?";
        Object[] selectParams = new Object[] { id };
        List<Video> video = jdbcTemplate.query( selectQuery, selectParams, new VideosRowMapper() );
        if ( video.isEmpty() ) {
            LOG.info( "No video found for id {}", id );
            return null;
        }
        return video.get( 0 );
    }

    /**
     * Get all the stored {@link sihoiba.interviewHomework.model.Video}s
     * @return the stored videos
     */
    public List<Video> getAllVideos() {
        String selectQuery = "SELECT id, title, date FROM mydb.videos ORDER by id ASC";
        Object[] selectParams = new Object[] {};
        List<Video> records = jdbcTemplate.query( selectQuery, selectParams, new VideosRowMapper() );

        if ( records.isEmpty() ) {
            LOG.info( "No videos found" );
        }
        return records;
    }

    /**
     * Gets all stored {@link sihoiba.interviewHomework.model.Video}s whose titles contains the matching search term, case insensitive matching
     * @param valueToMatch the value to check for
     * @return the videos with matching titles
     */
    public List<Video> findVideosWithMatchingTitle( String valueToMatch ) {
        Assert.notNull( valueToMatch, "valueToMatch must not be null" );

        String selectQuery = "SELECT id, title, date FROM mydb.videos WHERE UPPER(title) LIKE ?";
        String value = "%" + valueToMatch.toUpperCase() + "%";
        Object[] selectParams = new Object[] { value };
        List<Video> videos = jdbcTemplate.query( selectQuery, selectParams, new VideosRowMapper() );
        if ( videos.isEmpty() ) {
            LOG.info( "No video found with title {}", valueToMatch );
            return null;
        }
        return videos;
    }

    /**
     * Deletes the {@link sihoiba.interviewHomework.model.Video} with the corresponding id
     * @param id the id of the video to delete
     */
    public void delete( Long id ) {
        Assert.notNull( id, "id must not be null" );
        String query = "DELETE FROM mydb.videos WHERE id = ?";
        Object[] params = new Object[] { id };
        jdbcTemplate.update( query, params );
    }
}
