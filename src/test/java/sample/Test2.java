package hikari.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSetWriter;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test2 {
    private static final Logger logger = LoggerFactory.getLogger(Test2.class);

    IDatabaseConnection connection;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException, DatabaseUnitException,
            IOException {
        connection = DBConnection.from(DBConfig.fromProperty("/h2.properties", this.getClass()));
    }

    @Test
    public void testLoadXml() throws DatabaseUnitException, Exception {
        logger.info("testLoadXml開始");

        IDataSet ds = DataSet.loadXml("/data2.xml", "/dtd.xml", getClass());

        XlsDataSetWriter writer = new XlsDataSetWriter();
        writer.write(ds, new FileOutputStream("target/out.xls"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, ds);
        } finally {
            connection.close();
        }
    }

    @Test
    public void testLoadXls() throws DatabaseUnitException, Exception {
    
        logger.info("testLoadXls開始");
    
        IDataSet ds = DataSet.loadXls("/data1.xls", this.getClass());
    
        XlsDataSetWriter writer = new XlsDataSetWriter();
        writer.write(ds, new FileOutputStream("target/out3.xls"));
    
        try {
            // データベースにあってデータセットにないテーブルは影響を受けない
            DatabaseOperation.CLEAN_INSERT.execute(connection, ds);
        } finally {
            connection.close();
        }
    }

    @Test
    public void testAssertXml() throws DatabaseUnitException, Exception {
        logger.info("開始");

        IDataSet ds = DataSet.loadXml("/data2.xml", "/dtd.xml", getClass());
        IDataSet ds2 = DataSet.loadXml("/hikari/test/data3.xml", "/dtd.xml", getClass());
        // IDataSet ds2 = DataSet.loadXml("/hikari/test/data3.xml", "/dtd.xml",
        // getClass());

        Assertion.assertEquals(ds, ds2);
    }

    //@Test
    public void testIgnoreColumns() throws DatabaseUnitException {
        ITable actual = null;
        ITable expected = null;

        ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actual, expected
                .getTableMetaData().getColumns());
      
        Assertion.assertEquals(expected, filteredTable);
    }

}
