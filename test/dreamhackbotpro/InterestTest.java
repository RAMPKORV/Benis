/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patrik
 */
public class InterestTest {

    public InterestTest() {
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

    /**
     * Test of isBuying method, of class Interest.
     */
    @Test
    public void testThingInfo() {
        new Interest("snus", 50, true);
        new Interest("snus", 60, false);
        new Interest("snus", 65, true);
        new Interest("cigg", 40, true);
        new Interest("cigg", 50, false);
        ThingInfo first = Interest.getInterestsSorted().first();
        ThingInfo last = Interest.getInterestsSorted().last();
        assertEquals(first.getThing(), "snus");
        assertEquals(last.getThing(), "cigg");
        assertEquals(Interest.getInterestsSorted().size(), 2);
        assertEquals(first.getBuyers(), 2);
        assertEquals(first.getSellers(), 1);
        assertEquals(last.getBuyers(), 1);
        assertEquals(last.getSellers(), 1);
        assertEquals(first.getMedian(), 60.0f, 0.01f);
        assertEquals(last.getMedian(), 45.0f, 0.01f);
    }



}