package sihoiba.interviewHomework.model;

import javax.validation.constraints.NotNull;

/**
 * Request API object for the Search endpoint.
 */
public final class SearchTerm {

    @NotNull
    private SearchField searchField;

    @NotNull
    private String valueToMatch;

    public SearchTerm( SearchField searchField, String valueToMatch ) {
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
