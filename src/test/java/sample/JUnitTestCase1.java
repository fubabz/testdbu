package sample;

import java.sql.DriverManager;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * DbUnitサイトのサンプル DbTestCaseを拡張しない場合
 *
 * @author ryo
 *
 */
public class JUnitTestCase1 extends TestCase {
    java.sql.Connection con;

    private static java.sql.Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver");
        java.sql.Connection connection = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/~/db/sample", "sa", "");
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
        IDataSet dataSet = new XlsDataSet(getClass().getResourceAsStream("/data1.xls"));
//        IDataSet dataSet = new XlsDataSet(new File("ss"));
        ReplacementDataSet  ds1 = new ReplacementDataSet(dataSet);
        ds1.addReplacementObject("[null]", null);
        ds1.addReplacementObject("[NULL]", null);

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, ds1);
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
        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:h2:tcp://localhost/~/db/sample",
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
//        databaseTester.onTearDown();
    }

    @Test
    public void test() {
        assertTrue(true);
    }

}
