package sihoiba.interviewHomework.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

/**
 * The response contain the video details search results
 */
public final class VideoDetailsSearchResponse {

    private final List<VideoDetailsSearchResult> results;

    @JsonCreator
    public VideoDetailsSearchResponse( @JsonProperty( "results" ) List<VideoDetailsSearchResult> results ) {
        this.results = results;
    }

    public List<VideoDetailsSearchResult> getResults() {
        return results;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof VideoDetailsSearchResponse ) ) {
            return false;
        }
        VideoDetailsSearchResponse that = (VideoDetailsSearchResponse) o;
        return Objects.equals( results, that.results );
    }

    @Override
    public int hashCode() {
        return Objects.hash( results );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "results", results )
                .toString();
    }


}
