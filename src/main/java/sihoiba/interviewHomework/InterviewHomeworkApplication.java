package sihoiba.interviewHomework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class InterviewHomeworkApplication extends SpringBootServletInitializer {

	private static final Logger LOG = LoggerFactory.getLogger( InterviewHomeworkApplication.class );

	public static void main( String[] args ) {
		LOG.info( "Starting application" );
		new SpringApplicationBuilder().sources( InterviewHomeworkApplication.class ).run( args );
	}
}
