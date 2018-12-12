package sihoiba.interviewHomework.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * An abstract test class for RowMapper unit tests.
 * <p>
 * This generic abstract class provides methods to convert row data in a Map<String, Object> to
 * a java.sql.ResultSet for the purposes of testing RowMappers.
 * <p>
 * It provides a shouldMapRow test case that passes the input data defined in the subclass to the RowMapper
 * initialised in the subclass and asserts results against the expected output definition of the subclass.
 */
public abstract class AbstractRowMapperTest<T> {

	protected RowMapper<T> target;

	@Test
	public void shouldMapRow() throws SQLException {
		// Given
		ResultSet resultSet = rowDataAsResultSet();

		// When
		T record = target.mapRow(resultSet, 1);

		// Then
		assertThat(record).isEqualTo(getExpectedRecord());
	}

	protected ResultSet rowDataAsResultSet() throws SQLException {
		Map<String, Object> rowData = rowData();
		return resultSet(rowData);
	}

	protected ResultSet resultSet(Map<String, Object> rowData) throws SQLException {
		MockResultSet resultSet = new MockResultSet("test");

		for (Map.Entry<String, Object> item : rowData.entrySet()) {
			resultSet.addColumn(item.getKey(), Collections.singletonList(item.getValue()));
		}

		resultSet.next();
		return resultSet;
	}

	protected abstract Map<String, Object> rowData();

	protected abstract T getExpectedRecord();
}
