package sihoiba.interviewHomework.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Configuration class for DAO unit tests
 */
public class DaoTestConfiguration {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( "org.h2.Driver" );
        dataSource.setUrl( "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1" );
        dataSource.setUsername( "sa" );
        dataSource.setPassword( "" );

        Resource initSchema = new ClassPathResource( "youtube.sql" );
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator( initSchema );
        DatabasePopulatorUtils.execute( databasePopulator, dataSource );

        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate( DataSource dataSource ) {
        return new JdbcTemplate( dataSource );
    }
}
