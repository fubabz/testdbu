package hikari.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

public class DBConnection {

    /**
     * DB 接続情報を返す
     * 
     * @param config
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws DatabaseUnitException
     */
    public static IDatabaseConnection from(DBConfig config) throws ClassNotFoundException,
            SQLException, DatabaseUnitException {

        Class.forName(config.getDriver());

        Connection connection = DriverManager.getConnection(config.getUrl(), config.getUser(),
                config.getPassword());

        // DB毎接続用の別クラスがある
        IDatabaseConnection dc = new DatabaseConnection(connection, config.getSchema());
//        new OracleConnection(connection, config.getSchema());
//        new H2Connection(connection, config.getSchema());
        DatabaseConfig dbConfig = dc.getConfig();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
        return dc;
    }

}
