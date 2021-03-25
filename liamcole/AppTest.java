// package liamcole; //(because why cant java just work right)

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 * Unit tests 3780
 */
public class AppTest {
    // Test1(setType):
    @Test
    public void TestsetType() throws Exception {
        System.out.println("TestsetType");

        // Initializations:
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setType(0x40);// 1
        assertEquals(0x01, pt.type);

        h.setType(0x80);// 2
        assertEquals(0x02, pt.type);

        h.setType(0xC0);// 3
        assertEquals(0x03, pt.type);
    }

    // Test2(getType):
    @Test
    public void TestgetType() throws Exception {
        System.out.println("TestgetType");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.type = 0x01;
        assertEquals(0x01, h.getType());

        pt.type = 0x02;
        assertEquals(0x02, pt.type);

        pt.type = 0x03;
        assertEquals(0x03, pt.type);

    }

    @Test
    public void TestsetTr() {
        System.out.println("TestsetTR");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setTR(0xBB);
        assertEquals(0x1, pt.tr);
    }

    @Test
    public void TestgetTr() {
        System.out.println("TestgetTR");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.tr = (byte) 1;
        assertEquals(0x1, h.getTR());
    }

    @Test
    public void TestsetWindow() {
        System.out.println("TestsetWindow");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setWindow(0xBB);
        assertEquals(0x1B, pt.window);
    }

    @Test
    public void TestgetWindow() {
        System.out.println("TestgetWindow");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.window = (byte) 0x15;
        assertEquals(0x15, h.getWindow());
    }

    @Test
    public void TestsetSeqnum() {
        System.out.println("TestsetSeqnum");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt = h.retPacket();
        h.setSeqnum(0xFF);
        assertEquals((byte) 0xFF, (byte) pt.seqnum);
    }

    @Test
    public void TestgetSeqnum() {
        System.out.println("TestgetSeqnum");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.seqnum = (byte) 0xFF;
        assertEquals((byte) 0xFF, h.getSeqnum());
    }

    @Test
    public void TestsetLength() {
        System.out.println("TestsetLength");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();
        h.setLength(300);
        assertEquals((byte) 0x01, (byte) pt.length[0]);
        assertEquals((byte) 0x2C, (byte) pt.length[1]);
    }

    @Test
    public void TestgetLength() {
        System.out.println("TestgetLength");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();
        pt.length[0] = 0x01;
        pt.length[1] = 0x2C;
        assertEquals(300, h.getLength());
    }

    @Test
    public void TestsetTimestamp() {
        System.out.println("TestsetTimestamp");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setTimestamp(0xBBBBBBBB);
        assertEquals((byte) 0xBB, (byte) pt.timestamp[0]);
        assertEquals((byte) 0xBB, (byte) pt.timestamp[1]);
        assertEquals((byte) 0xBB, (byte) pt.timestamp[2]);
        assertEquals((byte) 0xBB, (byte) pt.timestamp[3]);
    }

    @Test
    public void TestgetTimestamp() {
        System.out.println("TestgetTimestamp");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.timestamp[0] = (byte) 0xBB;
        pt.timestamp[1] = (byte) 0xBB;
        pt.timestamp[2] = (byte) 0xBB;
        pt.timestamp[3] = (byte) 0xBB;
        assertEquals(0xBBBBBBBB, h.getTimestamp());
    }

    @Test
    public void TestsetCRC1() {
        System.out.println("TestsetCRC1");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setCRC1(0xBBBBBBBB);
        assertEquals((byte) 0xBB, (byte) pt.crc1[0]);
        assertEquals((byte) 0xBB, (byte) pt.crc1[1]);
        assertEquals((byte) 0xBB, (byte) pt.crc1[2]);
        assertEquals((byte) 0xBB, (byte) pt.crc1[3]);
    }

    @Test
    public void TestgetCRC1() {
        System.out.println("TestgetCRC1");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.crc1[0] = (byte) 0xBB;
        pt.crc1[1] = (byte) 0xBB;
        pt.crc1[2] = (byte) 0xBB;
        pt.crc1[3] = (byte) 0xBB;
        assertEquals(0xBBBBBBBB, h.getCRC1());
    }

    @Test
    public void TestsetCRC2() {
        System.out.println("TestsetCRC2");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        h.setCRC2(0xBBBBBBBB);
        assertEquals((byte) 0xBB, (byte) pt.crc2[0]);
        assertEquals((byte) 0xBB, (byte) pt.crc2[1]);
        assertEquals((byte) 0xBB, (byte) pt.crc2[2]);
        assertEquals((byte) 0xBB, (byte) pt.crc2[3]);
    }

    @Test
    public void TestgetCRC2() {
        System.out.println("TestgetCRC2");

        // Initializations
        Header h = new Header();
        Packet pt = h.retPacket();

        pt.crc2[0] = (byte) 0xBB;
        pt.crc2[1] = (byte) 0xBB;
        pt.crc2[2] = (byte) 0xBB;
        pt.crc2[3] = (byte) 0xBB;
        assertEquals(0xBBBBBBBB, h.getCRC2());
    }

    @Test
    public void TestsetPayload() throws UnsupportedEncodingException {
        System.out.println("TestsetPayload");

        Header h = new Header();
        Packet pt = h.retPacket();

        h.setPayload("hello");
        assertEquals(0x68, pt.payload[0]);
        assertEquals(0x65, pt.payload[1]);
        assertEquals(0x6c, pt.payload[2]);
        assertEquals(0x6c, pt.payload[3]);
        assertEquals(0x6f, pt.payload[4]);

    }

    @Test
    public void TestgetPayload() throws UnsupportedEncodingException {
        System.out.println("TestgetPayload");

        Header h = new Header();
        Packet pt = h.retPacket();

        pt.payload = new byte[5];
        pt.payload[0] = 0x68;
        pt.payload[1] = 0x65;
        pt.payload[2] = 0x6c;
        pt.payload[3] = 0x6c;
        pt.payload[4] = 0x6f;
        assertEquals("hello", h.getPayload());
    }
}
