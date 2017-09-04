package sample;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

/**
 * localhost/postgres/local テーブル準備.
 *
 */
@RunWith(SpringRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DbUnitConfiguration
@ContextConfiguration(classes = { TestPrepare.Config.class })
public class TestPrepare extends AbstractJunitTemplate {

    @Test
    @DatabaseSetup(value = { "ServiceTest.xml" }, type = DatabaseOperation.REFRESH)
    public void testPrepare() {
    }

    @Configuration
    public static class Config {
        private static final String URL = "jdbc:postgresql://localhost/postgres";
        private static final String USER = "postgres";
        private static final String PASSWORD = "postgres";
        private static final String SCHEMA = "local";

        // @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dmds = new DriverManagerDataSource(URL, USER, PASSWORD);
            return dmds;
        }

        @Bean
        public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() throws Exception {
            DatabaseDataSourceConnectionFactoryBean dbConnectionFactory =
                new DatabaseDataSourceConnectionFactoryBean(dataSource());

            dbConnectionFactory.setSchema(SCHEMA);
            DatabaseDataSourceConnection con = dbConnectionFactory.getObject();
            DatabaseConfig config = con.getConfig();
            DatabaseConfigBean databaseConfig = new DatabaseConfigBean();
            databaseConfig.apply(config);

            databaseConfig.setDatatypeFactory(new PostgresqlDataTypeFactory());
            dbConnectionFactory.setDatabaseConfig(databaseConfig);
            databaseConfig.setAllowEmptyFields(Boolean.TRUE);

            return dbConnectionFactory;
        }
    }
}
