package sihoiba.interviewHomework.integration;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Integration tests for the Video Details Controller endpoint.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@WebAppConfiguration
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD )
public class VideoDetailsIT {
}
