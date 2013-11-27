package hikari.test;

import java.sql.DriverManager;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DbUnitサイトのサンプル DbTestCaseを使わない場合
 *
 * @author ryo
 *
 */
public class Test1 /*extends TestCase*/ {
    java.sql.Connection con;

    private static java.sql.Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lookup", "postgres", "root");
        return connection;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     *
     */
    //@Override
    @Before
    public void setUp() throws Exception {
        // super.setUp();

        // initialize your database connection here
        IDatabaseConnection connection = new DatabaseConnection(getConnection());

        // initialize your dataset here
        // IDataSet dataSet = new XmlDataSet(new File("lookup"));

        FlatXmlDataFileLoader loader = new FlatXmlDataFileLoader();
        // loader.getBuilder().
        IDataSet dataSet = loader.load("/lookup.xml");
        try {
            DatabaseOperation.REFRESH.execute(connection, dataSet);
            // DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }

    private IDatabaseTester databaseTester;

    // @Override
    @After
    public void tearDown() throws Exception {
        // will call default tearDownOperation
        // databaseTester.onTearDown();
    }

    @Test
    public void test() {

    }

}
