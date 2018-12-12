package sihoiba.interviewHomework.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import sihoiba.interviewHomework.model.Video;

/**
 * A RowMapper unit test class for the Video.
 * <p>
 * Extends AbstractRowMapperTest with the Video type parameter.
 * <p>
 * Sets the target RowMapper to test and builds the expected Video result and
 * row data input.
 * <p>
 * The test method itself is in the abstract superclass.
 */
public class VideosRowMapperTest extends AbstractRowMapperTest<Video> {

	private static final Long ID_VALUE = 101L;
	private static final String TITLE_VALUE = "testTitle";
	private static final LocalDateTime DATE_VALUE = LocalDateTime.now();

	@Before
	public void setup() {
		target = new VideosRowMapper();
	}

	@Override
	protected Map<String, Object> rowData() {
		Map<String, Object> rowData = new HashMap<>();
		rowData.put( "id", ID_VALUE );
		rowData.put( "title", TITLE_VALUE );
		rowData.put( "date", Timestamp.valueOf( DATE_VALUE ) );
		return rowData;
	}

	@Override
	protected Video getExpectedRecord() {
		return new Video( ID_VALUE, TITLE_VALUE, DATE_VALUE );
	}
}
