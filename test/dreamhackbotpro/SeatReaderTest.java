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
public class SeatReaderTest {

    public SeatReaderTest() {
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
    public void testGetSeat() {
        SeatReader sr = new SeatReader();
        System.out.println("getSeat");
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på D12:34. Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på Rad D12 Plats 34 Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på Rad D 12 Plats 34 Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på D 12 Plats 34 Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på Rad D 12, Plats 34 Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på Rad: D12, Plats: 34 Kom hit!"));
        assertEquals("D12:34", SeatReader.getSeat("Jag sitter på Rad: D 12 Plats: 34 Kom hit!"));
        assertEquals("D43:44", SeatReader.getSeat("Jag sitter på D43 44"));
    }

}