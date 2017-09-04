package sample;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public abstract class AbstractJunitTemplate {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJunitTemplate.class);

    @Rule
    public TestName testName = new TestName();

    private static final PrintStream stdOut = System.out;
    private static final PrintStream stdErr = System.err;

    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        MDC.put("method", testName.getMethodName());
        logger.info("=====================================================================");
        logger.info(this.getClass().getSimpleName() + "#" + testName.getMethodName() + "： 開始");
    }

    @After
    public void down() {
        logger.info(this.getClass().getSimpleName() + "#" + testName.getMethodName() + "： 終了");
        MDC.put("method", "none");
    }

    protected void setStandard() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    protected void resetStandard() {
        System.setOut(stdOut);
        System.setErr(stdErr);
    }
}
