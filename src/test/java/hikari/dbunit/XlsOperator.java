package hikari.dbunit;
/**
 * 
 */


import java.io.FileInputStream;

import org.dbunit.database.DatabaseConfig;
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
                XlsOperator.execute(args[0], args[1], DatabaseOperation.REFRESH);
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
    public static void execute(String nameProp, String nameXls, DatabaseOperation operation)
            throws Exception {

        DBConfig config = DBConfig.fromProperty(nameProp, XlsOperator.class);
        ReplacementDataSet dataSet = new ReplacementDataSet(new XlsDataSet(new FileInputStream(
                nameXls)));
        dataSet.addReplacementObject("[NULL]", null);

        IDatabaseConnection dbConnection = DBConnection.from(config);
        DatabaseConfig dbConfig = dbConnection.getConfig();
        dbConfig.setProperty(ID_DATATYPE_FACTORY, Class.forName(config.getDatatype()).newInstance());

        try {
            operation.execute(dbConnection, dataSet);
        } finally {
            dbConnection.close();
        }
    }

}
