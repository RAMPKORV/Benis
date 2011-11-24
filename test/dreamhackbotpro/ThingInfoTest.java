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
public class ThingInfoTest {

    public ThingInfoTest() {
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
     * Test of getThing method, of class ThingInfo.
     */
    @Test
    public void testGetThing() {
        System.out.println("getThing");

    }

    /**
     * Test of getBuyers method, of class ThingInfo.
     */
    @Test
    public void testGetBuyers() {
        System.out.println("getBuyers");

    }

    /**
     * Test of getCounter method, of class ThingInfo.
     */
    @Test
    public void testGetCounter() {
        System.out.println("getCounter");

    }

    /**
     * Test of getSellers method, of class ThingInfo.
     */
    @Test
    public void testGetSellers() {
        System.out.println("getSellers");

    }

    /**
     * Test of getMedian method, of class ThingInfo.
     */
    @Test
    public void testGetMedian() {
        System.out.println("getMedian");

    }

    private static int counter = 0;
    private void addInterest(String thing, int price, boolean wtb, float certainty) {
        new User(new UserInfo(""+(counter++))).addInterest(new Interest(thing, price, wtb, certainty));
    }

    /**
     * Test of getStdDev method, of class ThingInfo.
     */
    @Test
    public void testGetStdDev() {
        System.out.println("getStdDev");
        addInterest("snus", 2, true, 1);
        addInterest("snus", 4, true, 1);
        addInterest("snus", 4, true, 1);
        addInterest("snus", 4, true, 1);
        addInterest("snus", 5, true, 1);
        addInterest("snus", 5, true, 1);
        addInterest("snus", 7, true, 1);
        addInterest("snus", 9, true, 1);
        ThingInfo snus = Interest.getInterestsSorted().get(0);
        assertEquals(2,snus.getStdDev(), 0.07);
    }

    /**
     * Test of addInterest method, of class ThingInfo.
     */
    @Test
    public void testAddInterest() throws Exception {
        System.out.println("addInterest");

    }

    /**
     * Test of med method, of class ThingInfo.
     */
    @Test
    public void testMed() {
        System.out.println("med");

    }

    /**
     * Test of compareTo method, of class ThingInfo.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");

    }

    /**
     * Test of getMaxPrice method, of class ThingInfo.
     */
    @Test
    public void testGetMaxPrice() {
        System.out.println("getMaxPrice");

    }

    /**
     * Test of getMinPrice method, of class ThingInfo.
     */
    @Test
    public void testGetMinPrice() {
        System.out.println("getMinPrice");

    }
    
    @Test
    public void testIsConfirmQuestion(){
        assertTrue(ThingInfo.isConfirmQuestion("korv?"));
    }

}