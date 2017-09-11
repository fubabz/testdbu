package hikari.dbunit;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

/**
 * Spring Test DBUnit 用設定基底.
 */
public abstract class AbstractSpringDbConfig {

    protected static DataSource dataSource(String driverClassName, String url, String schema, String username,
        String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        if (schema != null) {
            dataSource.setSchema(schema);
        }

        return dataSource;
    }

    protected static DatabaseDataSourceConnectionFactoryBean
        databaseDataSourceConnectionFactoryBean(DataSource dataSource, String schema) {
        DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean =
            new DatabaseDataSourceConnectionFactoryBean();
        databaseDataSourceConnectionFactoryBean.setDataSource(dataSource);
        databaseDataSourceConnectionFactoryBean.setSchema(schema);

        DatabaseDataSourceConnection con;
        try {
            con = databaseDataSourceConnectionFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("オブジェクト取得失敗", e);
        }

        DatabaseConfig config = con.getConfig();
        DatabaseConfigBean databaseConfig = new DatabaseConfigBean();
        databaseConfig.setAllowEmptyFields(Boolean.TRUE);
        databaseConfig.apply(config);

        databaseConfig.setDatatypeFactory(new PostgresqlDataTypeFactory());

        databaseDataSourceConnectionFactoryBean.setDatabaseConfig(databaseConfig);
        return databaseDataSourceConnectionFactoryBean;
    }

    protected static IDatabaseConnection
        iDatabaseConnection(DatabaseDataSourceConnectionFactoryBean connectionFactoryBean) throws Exception {
        return connectionFactoryBean.getObject();
    }
}
