package hikari.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
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

    // java.sql.Connection con;
    IDatabaseConnection connection;

    // private static java.sql.Connection getConnection() throws Exception {
    // String driver = "org.h2.Driver"; // "org.postgresql.Driver"
    // String url = "jdbc:h2:~/db/saku"; // "jdbc:postgresql:Training.dbunit";
    // String user = "sa"; // "postgres";
    // String password = "";
    //
    // Class.forName(driver);
    // java.sql.Connection connection = DriverManager.getConnection(url, user,
    // password);
    // return connection;
    // }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException, DatabaseUnitException,
            IOException {
        // initialize your database connection here
        connection = DBConnection.from(DBConfig.fromProperty("/h2.properties", this.getClass()));
        // connection = new H2Connection(getConnection(), "public");
        // connection = new DatabaseConnection(getConnection());
        // loadXsl();
    }

    @Test
    public void testLoadXml() throws DatabaseUnitException, Exception {
        logger.info("testLoadXml開始");

        // connection = new DatabaseConnection(getConnection());
        IDataSet ds = loadXml("/data2.xml");

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

        // connection = new DatabaseConnection(getConnection());
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

    private IDataSet loadXml(String name) throws DataSetException, FileNotFoundException {
        // クラスパスへ置く。/はトップレベル
        IDataSet ds = new FlatXmlDataFileLoader().load(name);
//        IDataSet ds2 = new FlatXmlDataSetBuilder().build(new FileInputStream(name));

        return ds;
    }

    private IDataSet loadXls(String name) {
//        IDataSet ds2 = new XlsDataSet(new File(name));

        DataFileLoader loader = new XlsDataFileLoader();
        Map<String, Object> replace = new HashMap<String, Object>();
        replace.put("[null]", null);

        // クラスパスへ置く。/はトップレベル
        loader.addReplacementObjects(replace);
        IDataSet ds = loader.load(name);

        return ds;
    }
}
