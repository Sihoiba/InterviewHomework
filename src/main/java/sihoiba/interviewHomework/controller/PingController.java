package sihoiba.interviewHomework.controller;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Controller that handles a ping request.
 */
@RestController
public class PingController {

    private static final Logger LOG = getLogger( PingController.class );

    @RequestMapping( path = "/ping", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE } )
    public String ping() {
        LOG.info( "Responding to PING request" );
        return "OK";
    }
}
