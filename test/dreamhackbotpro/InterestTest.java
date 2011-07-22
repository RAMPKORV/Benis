/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dreamhackbotpro;

import java.util.List;
import java.util.UUID;
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

    private static int counter = 0;
    private void addInterest(String thing, int price, boolean wtb, float certainty) {
        new User(""+(counter++)).addInterest(new Interest(thing, price, wtb, certainty));
    }

    /**
     * Test of isBuying method, of class Interest.
     */
    @Test
    public void testThingInfo() {
        addInterest("snus", 50, true, 1);
        addInterest("snus", 60, false, 1);
        addInterest("snus", 65, true, 1);
        addInterest("cigg", 40, true, 1);
        addInterest("cigg", 50, false, 1);
        List<ThingInfo> interestsSorted = Interest.getInterestsSorted();
        ThingInfo first = interestsSorted.get(0);
        ThingInfo last = interestsSorted.get(interestsSorted.size()-1);
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