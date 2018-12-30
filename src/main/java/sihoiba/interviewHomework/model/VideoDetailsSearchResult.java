package sihoiba.interviewHomework.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * A single search result for a search term
 */
public final class VideoDetailsSearchResult {

    private final Long id;
    private final String title;

    @JsonCreator
    public VideoDetailsSearchResult( @JsonProperty( "id" ) Long id, @JsonProperty( "title" ) String title ) {
        Assert.notNull( id, "id must not be null" );
        Assert.notNull( title, "id must not be null" );
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof VideoDetailsSearchResult ) ) {
            return false;
        }
        VideoDetailsSearchResult that = (VideoDetailsSearchResult) o;
        return Objects.equals( id, that.id ) &&
                Objects.equals( title, that.title );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, title );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "id", id )
                .add( "title", title )
                .toString();
    }
}
