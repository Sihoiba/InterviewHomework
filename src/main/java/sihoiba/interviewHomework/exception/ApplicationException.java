package sihoiba.interviewHomework.exception;

/**
 * Runtime exceptions thrown by the application.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException( String message, Throwable cause ) {
        super( message, cause );
    }
}
