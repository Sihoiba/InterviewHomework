package sihoiba.interviewHomework.persistence;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import sihoiba.interviewHomework.model.Channel;

/**
 * A RowMapper unit test class for the Channel.
 * <p>
 * Extends AbstractRowMapperTest with the Channel type parameter.
 * <p>
 * Sets the target RowMapper to test and builds the expected Channel result and
 * row data input.
 * <p>
 * The test method itself is in the abstract superclass.
 */
public class ChannelsRowMapperTest extends AbstractRowMapperTest<Channel> {

	private static final Long ID_VALUE = 101L;
	private static final String CHANNEL_NAME_VALUE = "testChannel";

	@Before
	public void setup() {
		target = new ChannelsRowMapper();
	}

	@Override
	protected Map<String, Object> rowData() {
		Map<String, Object> rowData = new HashMap<>();
		rowData.put( "id", ID_VALUE );
		rowData.put( "channel_name", CHANNEL_NAME_VALUE );
		return rowData;
	}

	@Override
	protected Channel getExpectedRecord() {
		return new Channel( ID_VALUE, CHANNEL_NAME_VALUE );
	}

}
