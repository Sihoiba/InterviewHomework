package sihoiba.interviewHomework.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.NotNull;

/**
 * Request API object for the Search endpoint.
 */
public final class SearchTerm {

    @NotNull
    private SearchField searchField;

    @NotNull
    private String valueToMatch;

    @JsonCreator
    public SearchTerm( @JsonProperty( "searchField" ) SearchField searchField,  @JsonProperty( "valueToMatch" ) String valueToMatch ) {
        this.searchField = searchField;
        this.valueToMatch = valueToMatch;
    }

    public String getValueToMatch() {
        return valueToMatch;
    }

    public SearchField getSearchField() {
        return searchField;
    }
}
