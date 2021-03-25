// package liamcole; //(because why cant java just work right)

import java.util.BitSet;

public class Packet {

    // Packet (contains header fields)
    protected byte type = 0;

    protected byte tr = 0;

    protected byte window = 0;

    protected byte seqnum = 0;

    protected byte[] length = new byte[2];

    protected byte[] timestamp = new byte[4];

    protected byte[] crc1 = new byte[4];

    protected byte[] payload = null;

    protected byte[] crc2 = new byte[4];

}
