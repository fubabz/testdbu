package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import org.junit.Test;

/**
 *
 * @author ryo
 *
 * @see http://dbunit.wikidot.com/demoimportexport
 *
 */
public class DemoDBUnit {
    private String testDir = "src/test/resources";
    private String dbFile = "dbDump.xml";
    private String testTableName = "t1";

    private String driverClassName = "org.h2.Driver";
    // "org.apache.derby.jdbc.EmbeddedDriver";
    private String dbUrl = "jdbc:h2:tcp://localhost/sample";
    // "jdbc:derby:testdb";
    private String dbUser = "sa";
    private String dbPassword = "";

    @Test
    public void test()
            throws ClassNotFoundException, DatabaseUnitException, IOException, SQLException {
        fullDatabaseImport(new File(testDir, dbFile));
        ITable actualTable = getConnection().createDataSet().getTable(testTableName);

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(testDir, dbFile));
        ITable expectedTable = expectedDataSet.getTable(testTableName);
        Assertion.assertEquals(expectedTable, actualTable);
    }

    public static void main(String[] args)
            throws ClassNotFoundException, DatabaseUnitException, IOException, SQLException {

        DemoDBUnit unit = new DemoDBUnit();
        unit.fullDatabaseExport();
    }

    public void fullDatabaseExport() throws ClassNotFoundException, DataSetException,
            DatabaseUnitException, IOException, SQLException {
        fullDatabaseExport(new File(testDir, dbFile));
    }

    public void fullDatabaseExport(File file) throws ClassNotFoundException, DatabaseUnitException,
            DataSetException, IOException, SQLException {
        IDatabaseConnection connection = getConnection();

        ITableFilter filter = new DatabaseSequenceFilter(connection);
        IDataSet dataset = new FilteredDataSet(filter, connection.createDataSet());
        FlatXmlDataSet.write(dataset, new FileOutputStream(file));
    }

    public void fullDatabaseImport(File file)
            throws ClassNotFoundException, DatabaseUnitException, IOException, SQLException {
        IDatabaseConnection connection = getConnection();
        IDataSet dataSet =  new FlatXmlDataSetBuilder().setDtdMetadata(true).build(file);

        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }

    public IDatabaseConnection getConnection()
            throws ClassNotFoundException, DatabaseUnitException, SQLException {
        @SuppressWarnings("unused")
        Class<?> driverClass = Class.forName(driverClassName);
        Connection jdbcConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        return new DatabaseConnection(jdbcConnection);
    }
}
