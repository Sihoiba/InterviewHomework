package sihoiba.interviewHomework.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sihoiba.interviewHomework.model.Channel;

public class ChannelsRowMapper implements RowMapper<Channel> {

	@Override
	public Channel mapRow( ResultSet resultSet, int rowNum ) throws SQLException {
			return new Channel(
				resultSet.getLong( "id" ),
				resultSet.getString( "channel_name" ) );
	}

}
