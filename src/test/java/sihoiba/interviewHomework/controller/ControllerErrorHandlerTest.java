package sihoiba.interviewHomework.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.mock.web.MockHttpServletRequest;
import sihoiba.interviewHomework.model.ErrorResponse;

/**
 * Test class for {@link ControllerErrorHandler}
 */
@RunWith( MockitoJUnitRunner.class )
public class ControllerErrorHandlerTest {

    @Mock
    private ErrorAttributes errorAttributes;

    @InjectMocks
    private ControllerErrorHandler classUnderTest;

    @Test
    public void shouldGetErrorPath() {
        // Given

        // When

        String actual = classUnderTest.getErrorPath();

        // Then
        assertThat( actual ).isEqualTo( "/error" );
    }

    @Test
    public void shouldHandleError() {
        // Given
        Date start = new Date();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put( "status", 400 );
        attributes.put( "error", "Bad Request" );
        attributes.put( "message", "That was a bad request" );
        attributes.put( "path", "/my/request/uri" );
        MockHttpServletRequest mockRequest = new MockHttpServletRequest( "GET", "/my/request/uri" );
        when( errorAttributes.getErrorAttributes( any(), anyBoolean() ) ).thenReturn( attributes );

        // When
        ErrorResponse actual = classUnderTest.handleError( mockRequest );
        Date end = new Date();

        // Then
        assertThat( actual ).isNotNull();
        assertThat( actual.getPath() ).isEqualTo( "/my/request/uri" );
        assertThat( actual.getMessage() ).isEqualTo( "That was a bad request" );
        assertThat( actual.getError() ).isEqualTo( "Bad Request" );
        assertThat( actual.getStatus() ).isEqualTo( "400" );
        assertThat( actual.getTimestamp() ).isBetween( start, end, true, true );
    }
}
