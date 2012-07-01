package hikari.test.generated;

public class HelloJet
{
  protected static String nl;
  public static synchronized HelloJet create(String lineSeparator)
  {
    nl = lineSeparator;
    HelloJet result = new HelloJet();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "Hello, JET. ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     String arg = (String)argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append( arg );
    return stringBuffer.toString();
  }
}
