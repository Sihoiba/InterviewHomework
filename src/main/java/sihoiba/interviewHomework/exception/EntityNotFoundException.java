package sihoiba.interviewHomework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when not matching entity is found.
 */
@ResponseStatus( value = HttpStatus.NOT_FOUND )
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException( String message ) {
        super( message );
    }

}
