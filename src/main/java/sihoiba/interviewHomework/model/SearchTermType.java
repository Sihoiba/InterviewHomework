package sihoiba.interviewHomework.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import sihoiba.interviewHomework.exception.SearchTermNotFoundException;

import java.util.Objects;

/**
 * Enum of the possible fields to search on.
 */
public enum SearchTermType {
    TITLE( "title" );

    private String value;

    SearchTermType( String value ) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SearchTermType fromValue( String value ) {
        if ( Objects.isNull( value ) ) {
            return null;
        }

        for( SearchTermType searchTermEnum : SearchTermType.values() ) {
            if ( searchTermEnum.getValue().equalsIgnoreCase( value ) ) {
                return searchTermEnum;
            }
        }
        throw new SearchTermNotFoundException( String.format( "The search term '%s' is not supported", value ) );
    }
}
