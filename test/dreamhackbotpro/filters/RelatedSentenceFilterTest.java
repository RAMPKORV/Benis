package dreamhackbotpro.filters;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wasd
 */
public class RelatedSentenceFilterTest extends MessageFilterTest{

    public RelatedSentenceFilterTest() {
        super(new RelatedSentenceFilter());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFilter() {
        checkFromTo("WTS snus. 50kr", "WTS snus 50kr");
        checkFromTo("WTS snus. WTB cigg 20kr.", "WTS snus. WTB cigg 20kr.");
        //TODO more tests
    }

}
