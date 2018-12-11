package sihoiba.interviewHomework.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import sihoiba.interviewHomework.model.Video;

public class VideosRowMapper implements RowMapper<Video> {

	@Override
	public Video mapRow( ResultSet resultSet, int rowNum ) throws SQLException {
		Timestamp dateTimestamp = resultSet.getTimestamp( "date" );
		return new Video(
				resultSet.getLong( "id" ),
				resultSet.getString( "title" ),
				dateTimestamp.toLocalDateTime() );
	}

}
