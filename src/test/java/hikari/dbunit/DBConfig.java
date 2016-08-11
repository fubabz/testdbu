package hikari.dbunit;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DB 接続情報
 * 
 * @author ryo
 * 
 */
public class DBConfig {
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
    public static DBConfig fromProperty(String name, Class<?> clazz) throws IOException {
        DBConfig dbConfig = new DBConfig();
        // ResourceBundle rb = ResourceBundle.getBundle(name);
        Properties properties = new Properties();
        InputStream is = clazz.getResourceAsStream(name);
        if (is == null) {
            throw new RuntimeException(name + ": クラスパスになし");
        }

        properties.load(is);

        dbConfig.driver = properties.getProperty("db.driver");
        dbConfig.url = properties.getProperty("db.url");
        dbConfig.user = properties.getProperty("db.user");
        dbConfig.password = properties.getProperty("db.password");
        dbConfig.schema = properties.getProperty("db.schema");
        dbConfig.datatype = properties.getProperty("db.datatype");

        return dbConfig;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public String getDatatype() {
        return datatype;
    }
}