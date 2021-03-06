package sample;

import java.io.FileInputStream;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

/**
 * DbUnitサイトのサンプル DBTestCase を拡張する場合
 *
 * @author ryo
 *
 */
public class DbTestCase1 extends DBTestCase {

    public DbTestCase1(String name) {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "org.h2.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:h2:tcp://localhost/~/db/sample");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
        // System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
        // "" );
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/data2.xml"));
//        return new FlatXmlDataSetBuilder().build(new FileInputStream("dataset.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }

    @Test
    public void test() {
        assertTrue(true);
    }
}
