package hikari.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.dbunit.util.fileloader.XlsDataFileLoader;

public class DataSet {
    private static final String BACKUP_FILENAME = "dbunit_backup";
    private static Map<String, Object> mapReplace = new HashMap<String, Object>();

    static {
        mapReplace.put("[null]", null);
    }

    public static IDataSet loadXml(String name, String dtdName, Class<?> clazz)
            throws DataSetException, IOException {
        // クラスパスへ置く。/はトップレベル
        // IDataSet ds = new FlatXmlDataFileLoader().load(name);

        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder().setMetaDataSetFromDtd(clazz
                .getResourceAsStream(dtdName));
        FlatXmlDataFileLoader loader = new FlatXmlDataFileLoader(builder);
        loader.addReplacementObjects(mapReplace);

        IDataSet ds = loader.load(absPath(name, clazz));
        // IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new
        // File("expectedDataSet.xml"));
        return ds;
    }

    public static IDataSet loadXls(String name, Class<?> clazz) {
        // IDataSet ds2 = new XlsDataSet(new File(name));

        DataFileLoader loader = new XlsDataFileLoader();

        // クラスパスへ置く。/はトップレベル
        loader.addReplacementObjects(mapReplace);

        IDataSet ds = loader.load(absPath(name, clazz));

        return ds;
    }

    private static String absPath(String name, Class<?> clazz) {
        if (!name.startsWith("/")) {
            URL url = clazz.getResource(name);
            if (url == null) {
                throw new IllegalArgumentException(name + ": Not found.");
            }
            name = "/"
                + StringUtils.substringAfter(url.toString(), clazz.getResource("/")
                    .toString());
        }
        return name;
    }

    /**
     * <p>[概 要] saveTables メソッド。</p>
     * <p>[詳 細] 指定テーブルのバックアップファイルを作成する。</p>
     * <p>[備 考] </p>
     * 
     * @param connection DBUnit DB接続
     * @param tableNames テーブル名のリスト
     * @return バックアップファイル
     * @throws IOException
     * @throws DataSetException
     */
    public static File saveTables(IDatabaseConnection connection, String... tableNames)
        throws IOException, DataSetException {
        QueryDataSet dsOld = new QueryDataSet(connection);
        for (String tableName : tableNames) {
            dsOld.addTable(tableName);
        }
        File backupFile = File.createTempFile(BACKUP_FILENAME, ".xml");
        FlatXmlDataSet.write(dsOld, new FileOutputStream(backupFile));

        return backupFile;
    }

    /**
     * <p>[概 要] saveTables メソッド。</p>
     * <p>[詳 細] 指定テーブルのバックアップファイルを作成する。</p>
     * <p>[備 考] </p>
     *
     * @param filename ファイル名
     * @param connection DB接続
     * @param tableNames テーブル名のリスト
     * @return バックアップファイル
     * @throws IOException
     * @throws DataSetException
     */
    public static File saveTables(String filename, IDatabaseConnection connection, String... tableNames)
        throws IOException, DataSetException {
        QueryDataSet dsOld = new QueryDataSet(connection);
        for (String tableName : tableNames) {
            dsOld.addTable(tableName);
        }
        File backupFile = new File(filename);
        FlatXmlDataSet.write(dsOld, new FileOutputStream(backupFile));

        return backupFile;
    }

    /**
     * <p>[概 要] restoreTables メソッド。</p>
     * <p>[詳 細] バックアップファイルからテーブルデータをDBへ書き戻す。</p>
     * <p>[備 考] </p>
     * 
     * @param connection DBUnit DB接続
     * @param backupFile バックアップファイル
     * @throws MalformedURLException
     * @throws DatabaseUnitException
     * @throws SQLException
     */
    public static void restoreTables(IDatabaseConnection connection, File backupFile)
        throws MalformedURLException, DatabaseUnitException, SQLException {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(backupFile);
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }

    /**
     * <p>[概 要] assertTable メソッド。</p>
     * <p>[詳 細] 指定テーブルについて、期待値データと実データを比較する。</p>
     * <p>[備 考] </p>
     *
     * @param tableName テーブル名
     * @param expectedDataSetName 期待値データファイル名(Excel形式)
     * @param clazz 期待値データファイル名探索用基準クラス
     * @param connection DBUnit DB接続
     * @throws DataSetException
     * @throws SQLException
     * @throws DatabaseUnitException
     */
    public static void assertTable(String tableName, String expectedDataSetName,
        Class<?> clazz, IDatabaseConnection connection) throws 
        SQLException, DatabaseUnitException {
        
        // ファイルからテーブル期待値を読む
        
        IDataSet ds = DataSet.loadXls(expectedDataSetName, clazz);
        ITable expectedTable = ds.getTable(tableName);

        // データベースから実テーブルを読む
        
        IDataSet databaseDataSet = connection.createDataSet();
        ITable actualTable = databaseDataSet.getTable(tableName);

        // テーブル期待値に含まれているカラムのみを含むテーブルを実テーブルから作成する
        // 実行時に決まるシーケンス値、日付などを比較対象から外すため
        
        ITable filterdTable = DefaultColumnFilter.includedColumnsTable(actualTable,
            expectedTable.getTableMetaData().getColumns());

        // テーブルを比較する
        // DataSet内全テーブルの比較もできる
        Assertion.assertEquals(expectedTable, filterdTable);
    }
}
