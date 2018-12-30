package sihoiba.interviewHomework.model;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * The record representing a channel
 */
@Entity
@Table( name = "channels" )
public class Channel {

    @Id
    @GeneratedValue
    private Long id;
    private String channelName;

    public Channel( String channelName ) {
        this.channelName = channelName;
    }

    public Channel( Long id, String channelName ) {
        this.id = id;
        this.channelName = channelName;

    }

    public Long getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Channel ) ) {
            return false;
        }
        Channel that = (Channel) o;
        return Objects.equals( id, that.id ) &&
                Objects.equals( channelName, that.channelName );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id, channelName );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "id", id )
                .add( "channelName", channelName )
                .toString();
    }
}
