package sihoiba.interviewHomework.config;

import org.springframework.core.env.Environment;

/**
 * Environment settings for the application
 */
public class EnvironmentSettings {

    private String youtubeApiToken;

    public EnvironmentSettings( Environment env ) {
        this.youtubeApiToken = env.getRequiredProperty( "interview-homework.youtube.api.token" );
    }

    public String getYoutubeApiToken() {
        return youtubeApiToken;
    }
}
