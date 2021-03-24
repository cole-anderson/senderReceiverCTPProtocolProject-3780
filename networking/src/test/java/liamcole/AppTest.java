package liamcole;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

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

    @Test
    public void TestsetType() throws Exception {
        Header h = new Header();
        Packet pt = new Packet();

        pt = h.p; // ptr->
        h.setType(0x40);// 1
        assertEquals(0x01, pt.type);
        pt = h.p; // ptr->
        h.setType(0x80);// 2
        assertEquals(0x02, h.getType());
        pt = h.p; // ptr->
        h.setType(0xC0);// 3
        assertEquals(0x03, h.getType());

        // test if exception (TODO: LATER?)

    }

    @Test
    public void TestsetTr() {
        Header h = new Header();
        Packet pt = new Packet();

        pt = h.p;
        h.setTR(0xBB);
        assertEquals(0x1, pt.tr);
    }

    @Test
    public void TestsetWindow() {
        Header h = new Header();
        Packet pt = new Packet();

        pt = h.p;
        h.setWindow(0xBB);
        assertEquals(0x1B, pt.window);
    }
}
