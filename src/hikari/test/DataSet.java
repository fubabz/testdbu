package hikari.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.dbunit.util.fileloader.XlsDataFileLoader;

public class DataSet {
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

        IDataSet ds = loader.load(name);
        // IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new
        // File("expectedDataSet.xml"));
        return ds;
    }

    public static IDataSet loadXls(String name) {
        // IDataSet ds2 = new XlsDataSet(new File(name));

        DataFileLoader loader = new XlsDataFileLoader();

        // クラスパスへ置く。/はトップレベル
        loader.addReplacementObjects(mapReplace);
        IDataSet ds = loader.load(name);

        return ds;
    }

}
