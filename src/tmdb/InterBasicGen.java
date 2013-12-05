package tmdb;

public class InterBasicGen
{
  protected static String nl;
  public static synchronized InterBasicGen create(String lineSeparator)
  {
    nl = lineSeparator;
    InterBasicGen result = new InterBasicGen();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "delete from application.inter_ext ext where ext.inter_basic_id in (select inter_basic_id from application.inter_basic basic where basic.contract_id='";
  protected final String TEXT_2 = "');" + NL + "delete from application.inter_basic where contract_id='";
  protected final String TEXT_3 = "';";
  protected final String TEXT_4 = NL + "INSERT" + NL + "INTO application.inter_basic(" + NL + "  inter_basic_id" + NL + "  , contract_id" + NL + "  , company_name" + NL + "  , employee_number" + NL + "  , additional_duty_code" + NL + "  , family_name" + NL + "  , given_name" + NL + "  , create_from" + NL + "  , display_type" + NL + "  , invalid_flg" + NL + "  , delete_flg" + NL + "  , create_date" + NL + "  , create_by" + NL + "  , update_date" + NL + "  , update_by" + NL + ")" + NL + "VALUES (";
  protected final String TEXT_5 = NL + "  ";
  protected final String TEXT_6 = NL + "  , '";
  protected final String TEXT_7 = "'" + NL + "  , '凸凹商事'" + NL + "  , 'shain";
  protected final String TEXT_8 = "'" + NL + "  , '00'" + NL + "  , '鈴木'" + NL + "  , '";
  protected final String TEXT_9 = "子'" + NL + "  , '00'" + NL + "  , '00'" + NL + "  , '0'" + NL + "  , '0'" + NL + "  , '2013-12-01'" + NL + "  , 'sakuma'" + NL + "  , '2013-12-01'" + NL + "  , 'sakuma'" + NL + ");" + NL;
  protected final String TEXT_10 = NL + "INSERT" + NL + "INTO application.inter_ext(" + NL + "  inter_basic_id" + NL + "  , inter_ext_item_id" + NL + "  , value_char" + NL + "  , display_type" + NL + "  , create_date" + NL + "  , create_by" + NL + "  , update_date" + NL + "  , update_by" + NL + ")" + NL + "VALUES (";
  protected final String TEXT_11 = NL + "    ";
  protected final String TEXT_12 = NL + "  , '";
  protected final String TEXT_13 = "'" + NL + "  , '";
  protected final String TEXT_14 = "@somecompany.com'" + NL + "  , '00'" + NL + "  , '2013-12-01'" + NL + "  , 'sakuma'" + NL + "  , '2013-12-01'" + NL + "  , 'sakuma'" + NL + ");" + NL;
  protected final String TEXT_15 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     String contractId = "T000020";
// String contractId = "T000600";
int numBasic = 4;
int maxNumExt = 3;

    stringBuffer.append(TEXT_1);
    stringBuffer.append( contractId );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( contractId );
    stringBuffer.append(TEXT_3);
     for(int i=0; i<numBasic; i++){
int interBasicId = i+40000; 
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    stringBuffer.append( interBasicId );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( contractId );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( i );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( i );
    stringBuffer.append(TEXT_9);
     for (int j=0; j< i%(maxNumExt+1); j++){
String extItemId = null;
switch(j){
case 0:
extItemId = "EMAILEXT";
break;
case 1:
extItemId = "HOME_PHONE";
break;

case 2:
extItemId = "EMAIL2";
break;

case 3:
extItemId = "EMAIL3";
break;

case 4:
extItemId = "HOMEPAGE";
break;

case 5:
extItemId = "LOCATION_CODE";
break;

case 6:
extItemId = "LOCATION_NAME";
break;

case 7:
extItemId = "PRIVATE_MOBILE";
break;
case 8:
extItemId =  "URL";
break;
}


    stringBuffer.append(TEXT_10);
    stringBuffer.append(TEXT_11);
    stringBuffer.append( interBasicId );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( extItemId );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( interBasicId );
    stringBuffer.append(TEXT_14);
     } 
     } 
    stringBuffer.append(TEXT_15);
    return stringBuffer.toString();
  }
}
