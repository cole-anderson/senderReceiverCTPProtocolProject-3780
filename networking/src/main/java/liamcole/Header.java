package liamcole;

public class Header {

    public int x;

    // 32 bits == 4 bytes & 4096 bits = 512 bytes

    // (4 bytes) Type|TR|Window|Seqnum|Length
    // (4 bytes) Timestamp
    // (4 bytes) CRC1
    // (up to 512 bytes) Payload
    // (4 bytes) CRC2

    /**
     * MEMBERS:
     **/
    // Header:

    // Data:
    // private byte[] timestamp = new byte[4];

    // private byte[] crc1 = new byte[4];

    // private byte[] payload = new byte[length];

    // private byte[] crc2 = new byte[4];

    // Payload (512 bytes)

    // Constructors:
    public Header(int in) {
        this.x = in;
    }

    public int getHeader() {
        return x;
    }

    // Methods:
}
