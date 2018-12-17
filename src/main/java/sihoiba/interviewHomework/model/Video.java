package sihoiba.interviewHomework.model;

import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * The stored details of a video
 */
public final class Video {

    private Long id;
    private String title;
    private LocalDateTime publishedAt;

    public Video( String title, LocalDateTime publishedAt ) {
        this.title = title;
        this.publishedAt = publishedAt;
    }

    public Video( Long id, String title, LocalDateTime publishedAt ) {
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
        if ( !( o instanceof Video ) ) {
            return false;
        }
        Video that = (Video) o;
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
