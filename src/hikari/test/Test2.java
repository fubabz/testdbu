package hikari.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSetWriter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.dbunit.util.fileloader.XlsDataFileLoader;
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

        IDataSet ds = loadXml("/data2.xml", "/dtd.xml");

        XlsDataSetWriter writer = new XlsDataSetWriter();
        writer.write(ds, new FileOutputStream("target/out.xls"));

        try {
            DatabaseOperation.CLEAN_INSERT.execute(connection, ds);
        } finally {
            connection.close();
        }
    }

    @Test
    public void testAssertXml() throws DatabaseUnitException, Exception {
        logger.info("開始");

        IDataSet ds = loadXml("/data2.xml", "/dtd.xml");
        IDataSet ds2 = loadXml("data3.xml", "/dtd.xml");

        Assertion.assertEquals(ds, ds2);
    }

    @Test
    public void testLoadXls() throws DatabaseUnitException, Exception {

        logger.info("testLoadXls開始");

        IDataSet ds = loadXls("/data1.xls");

        XlsDataSetWriter writer = new XlsDataSetWriter();
        writer.write(ds, new FileOutputStream("target/out3.xls"));

        try {
            // データベースにあってデータセットにないテーブルは影響を受けない
            DatabaseOperation.CLEAN_INSERT.execute(connection, ds);
        } finally {
            connection.close();
        }
    }

    private IDataSet loadXml(String name, String dtdName) throws DataSetException, IOException {
        // クラスパスへ置く。/はトップレベル
        // IDataSet ds = new FlatXmlDataFileLoader().load(name);

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder()
                .setMetaDataSetFromDtd(getClass().getResourceAsStream(dtdName));
        FlatXmlDataFileLoader loader = new FlatXmlDataFileLoader(builder);

        Map<String, Object> replace = new HashMap<String, Object>();
        replace.put("[null]", null);
        loader.addReplacementObjects(replace);

        IDataSet ds = loader.load(name);
        // IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new
        // File("expectedDataSet.xml"));
        return ds;
    }

    private IDataSet loadXls(String name) {
        // IDataSet ds2 = new XlsDataSet(new File(name));

        DataFileLoader loader = new XlsDataFileLoader();
        Map<String, Object> replace = new HashMap<String, Object>();
        replace.put("[null]", null);

        // クラスパスへ置く。/はトップレベル
        loader.addReplacementObjects(replace);
        IDataSet ds = loader.load(name);

        return ds;
    }
}
