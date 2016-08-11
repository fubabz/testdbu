package hikari.test;

import java.io.File;
import java.sql.DriverManager;

import junit.framework.TestCase;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

/**
 * DbUnitサイトのサンプル DbTestCaseを拡張しない場合
 * 
 * @author ryo
 * 
 */
public class JUnitTestCase1 extends TestCase {
    java.sql.Connection con;

    private static java.sql.Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:postgresql:Training.dbunit", "postgres", "");
        return connection;
    }

    /*
     * 自分でDBオペレーションを実行する場合
     * 
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // initialize your database connection here
        IDatabaseConnection connection = new DatabaseConnection(getConnection());

        // initialize your dataset here
        IDataSet dataSet = new XlsDataSet(new File("ss"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }

    private IDatabaseTester databaseTester;

    /**
     * IDatabaseTester を使う場合
     * 
     * @throws Exception
     */
    protected void setUpByTester() throws Exception {
        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:sample",
                "sa", "");

        // initialize your dataset here
        IDataSet dataSet = null;
        // ...

        databaseTester.setDataSet(dataSet);
        // will call default setUpOperation
        databaseTester.onSetup();
    }

    @Override
    protected void tearDown() throws Exception {
        // will call default tearDownOperation
        databaseTester.onTearDown();
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
