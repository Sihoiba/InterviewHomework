package sihoiba.interviewHomework.model;

import org.junit.Test;
import sihoiba.interviewHomework.exception.SearchTermNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * Unit test for {@link SearchTermType}
 */
public class SearchTermTypeTest {

    @Test
    public void shouldGetTitleEnumGivenTitleValue() {
        // Given
        String value = "Title";

        // When
        SearchTermType result = SearchTermType.fromValue( value );

        // Then
        assertThat( result ).isEqualTo( SearchTermType.TITLE );
    }

    @Test
    public void shouldReturnNullGivenNullValue() {
        // Given
        String value = null;

        // When
        SearchTermType result = SearchTermType.fromValue( value );

        // Then
        assertThat( result ).isNull();
    }

    @Test
    public void shouldFailToGetFromValueGivenUnrecognisedValue() {
        // Given
        String value = "something else";

        try {
            // When
            SearchTermType.fromValue( value );
            shouldHaveThrown( SearchTermNotFoundException.class );
        } catch ( SearchTermNotFoundException expected ) {
            assertThat( expected ).hasMessage( "The search term 'something else' is not supported" );
        }
    }
}
