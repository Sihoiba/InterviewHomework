package sihoiba.interviewHomework.model;

import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class VideoDetails {

    private Long id;
    private String title;
    private LocalDateTime publishedAt;

    public VideoDetails( Long id, String title, LocalDateTime publishedAt ) {
        Assert.notNull( id, "id must not be null" );
        Assert.notNull( title, "id must not be null" );
        Assert.notNull( publishedAt, "id must not be null" );
        this.id = id;
        this.title = title;
        this.publishedAt = publishedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof VideoDetails ) ) {
            return false;
        }
        VideoDetails that = (VideoDetails) o;
        return Objects.equals( id, that.id ) &&
                Objects.equals( title, that.title ) &&
                Objects.equals( publishedAt, that.publishedAt );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, title, publishedAt );
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = publishedAt.format(formatter);
        return MoreObjects.toStringHelper( this )
                .add( "id", id )
                .add( "title", title )
                .add( "publishedAt", formatDateTime )
                .toString();
    }
}
