/**
 * 
 */
package hikari.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 * ExcelベースDBUnit実行
 * 
 * @author ryo
 * 
 */
public class XlsOperator {
	private static final String ID_DATATYPE_FACTORY = "http://www.dbunit.org/properties/datatypeFactory";

	/**
	 * @param args
	 *            プロパティ基底名, Excelファイル名
	 */
	public static void main(String[] args) {
		try {
			if (args.length >= 2) {
				XlsOperator
						.execute(args[0], args[1], DatabaseOperation.REFRESH);
			} else {
				throw new Exception("引数不足");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * DBUnit オペレーションを実行する
	 * 
	 * @param nameProp
	 *            接続情報プロパティ基底名
	 * @param nameXls
	 *            データセットファイル名
	 * @param operation
	 *            DBUnitオペレーション
	 * @throws Exception
	 */
	public static void execute(String nameProp, String nameXls,
			DatabaseOperation operation) throws Exception {

		DBConfig config = DBConfig.fromProperty(nameProp);
		ReplacementDataSet dataSet = new ReplacementDataSet(new XlsDataSet(
				new FileInputStream(nameXls)));
		dataSet.addReplacementObject("[NULL]", null);

		IDatabaseConnection dbConnection = getDBConnection(config);
		DatabaseConfig dbConfig = dbConnection.getConfig();
		dbConfig.setProperty(ID_DATATYPE_FACTORY, Class
				.forName(config.datatype).newInstance());

		try {
			operation.execute(dbConnection, dataSet);
		} finally {
			dbConnection.close();
		}
	}

	/**
	 * DB 接続情報を返す
	 * 
	 * @param config
	 *            DB 接続情報
	 * @return DB 接続
	 * @throws Exception
	 */
	private static IDatabaseConnection getDBConnection(DBConfig config)
			throws Exception {

		Class.forName(config.driver);

		Connection connection = DriverManager.getConnection(config.url,
				config.user, config.password);

		// TODO DB毎接続用の別クラスがある
		IDatabaseConnection dc = new DatabaseConnection(connection,
				config.schema);
		// IDatabaseConnection dc = new OracleConnection(connection,
		// config.schema);
		return dc;
	}

	/**
	 * DB 接続情報
	 * 
	 * @author ryo
	 * 
	 */
	private static class DBConfig {
		private String driver;
		private String url;
		private String user;
		private String password;
		private String schema;
		private String datatype;

		/**
		 * プロパティファイルからDB接続情報を作成する
		 * 
		 * @param name
		 *            接続情報プロパティ基底名
		 * @return DB接続情報
		 * @throws IOException
		 */
		private static DBConfig fromProperty(String name) throws IOException {
			DBConfig dbConfig = new DBConfig();
			ResourceBundle rb = ResourceBundle.getBundle(name);

			dbConfig.driver = rb.getString("db.driver");
			dbConfig.url = rb.getString("db.url");
			dbConfig.user = rb.getString("db.user");
			dbConfig.password = rb.getString("db.password");
			dbConfig.schema = rb.getString("db.schema");
			dbConfig.datatype = rb.getString("db.datatype");

			return dbConfig;
		}
	}
}
