package sihoiba.interviewHomework.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * A standard RESTful error response for the API to return.  All exceptions returned by the API should be represented
 * using this class as the HTTP response.
 */
public final class ErrorResponse {

    /**
     * The HTTP status code as a string
     */
    private String status;
    /**
     * The error code; either an internal error code or the HTTP status error e.g. 'Method Not Allowed'
     */
    private String error;
    /**
     * A descriptive message regarding the error
     */
    private String message;
    /**
     * The requested resource path that cause the error
     */
    private String path;
    /**
     * A timestamp the error occurred
     */
    private Date timestamp;

    @JsonCreator
    public ErrorResponse( @JsonProperty( "status" ) String status, @JsonProperty( "error" ) String error,
                          @JsonProperty( "message" ) String message, @JsonProperty( "path" ) String path, @JsonProperty( "timestamp" ) Date timestamp ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "status", status )
                .add( "error", error )
                .add( "message", message )
                .add( "path", path )
                .add( "timestamp", timestamp )
                .toString();
    }
}

