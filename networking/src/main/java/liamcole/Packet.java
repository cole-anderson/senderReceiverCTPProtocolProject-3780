package liamcole;

import java.util.BitSet;

public class Packet {
    // Packet (contains header fields)
    protected BitSet type = new BitSet(2);

    protected BitSet tr = new BitSet(1);

    protected BitSet window = new BitSet(5);

    protected byte seqnum = 0;

    protected byte[] length = new byte[2];

    protected byte[] timestamp = new byte[4];

    protected byte[] crc1 = new byte[4];

    protected byte[] payload = new byte[512];

    protected byte[] crc2 = new byte[4];

}
