package sihoiba.interviewHomework.util;

import com.google.api.client.util.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class GoogleDateTimeConverter {

    private static final Logger LOG = LoggerFactory.getLogger( GoogleDateTimeConverter.class );

    /**
     * Convert Google's DateTime object to a LocalDateTime.
     *
     * @param dt The original DateTime object
     * @return A parsed java LocalDateTime object
     * @throws Exception if the returned DateTime does not contain time information or the format is not as expected
     */
    public static final LocalDateTime getDateTime( DateTime dt ) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" );
            return LocalDateTime.parse( dt.toStringRfc3339(), format );
        } catch ( RuntimeException e ) {
            LOG.error( "Cannot parse googles datetime format", e );
            throw e;
        }
    }
}
