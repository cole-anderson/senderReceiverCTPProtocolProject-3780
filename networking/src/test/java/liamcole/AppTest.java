package liamcole;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        // this dumb stuff kinda works lol
        Header h = new Header();
        // int dec = Integer.parseInt("0011", 2);
        // h.setMsb(dec << 3);
        // int v = h.getMsb();
        // assertEquals(3, (v >> 3));

        h.setLength(300);
        assertEquals(300, h.getLength());

        // byte stuff
        /*
         * 
         * void setType(int val) { //val == 0011 //val 0000011111 }
         * 
         */

    }
}
