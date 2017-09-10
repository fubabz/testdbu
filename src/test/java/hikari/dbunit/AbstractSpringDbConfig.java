package hikari.dbunit;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.database.IDatabaseConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Spring Test DBUnit 用設定基底.
 */
public abstract class AbstractSpringDbConfig {

    protected static DataSource dataSource(String driverClassName, String url, String schema, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setSchema(schema);

        return dataSource;
    }

    protected static DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean(DataSource dataSource, String schema) {
        DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean = new DatabaseDataSourceConnectionFactoryBean();
        databaseDataSourceConnectionFactoryBean.setDataSource(dataSource);
        databaseDataSourceConnectionFactoryBean.setSchema(schema);

//        DatabaseConfigBean config = new DatabaseConfigBean();
//        databaseDataSourceConnectionFactoryBean.setDatabaseConfig(config);
        return databaseDataSourceConnectionFactoryBean;
    }

    protected static IDatabaseConnection iDatabaseConnection(DatabaseDataSourceConnectionFactoryBean connectionFactoryBean) throws Exception {
        return connectionFactoryBean.getObject();
    }
}
