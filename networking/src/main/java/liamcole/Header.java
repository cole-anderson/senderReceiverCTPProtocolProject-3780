package liamcole;
import java.util.*;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

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

    enum Type {
      DATA,
      ACK,
      NACK
    }

    // Data:
    private BitSet type = new BitSet(2);

    private BitSet tr = new BitSet(1);

    private BitSet window = new BitSet(5);

    private byte seqnum = 0;

    private byte[] length = new byte[2];

    private byte[] timestamp = new byte[4];

    private byte[] crc1 = new byte[4];

    private byte[] payload = new byte[512];

    private byte[] crc2 = new byte[4];

    //Payload (512 bytes)

    // Constructors:
    public Header(int in) {
        this.x = in;
    }

    public int getHeader() {
        return x;
    }

    // Methods:
    public BitSet getType() {
      return type;
    }

    public BitSet getTR() {
      return tr;
    }

    public BitSet getWindow() {
      return window;
    }

    public byte getSeqnum() {
      return seqnum;
    }

    public byte[] getLength() {
      return length;
    }

    public byte[] getTimeStamp() {
      return timestamp;
    }

    public byte[] getCRC1() {
      return crc1;
    }

    public byte[] getCRC2() {
      return crc2;
    }

    public String getPayload() {
      String str = new String(payload);
      return str;
    }

    public void setType(Type types) {
      switch(types) {
        case DATA:
          type.set(1);
          break;
        case ACK:
          type.set(0);
          break;
        case NACK:
          type.set(1);
          type.set(0);
          break;
      }
    }

    public void setTR() {
      tr.set(0);
    }

    public void setWindow(int val) {
      int i = 0;
      while (val != 0) {
        if(val % 2 != 0) {
          window.set(i);
        }
        ++i;
        val = val >> 1;
      }
    }

    public void setSeqnum(int seq) {
      seqnum = (byte)seq;
    }

    public void setLength(int len) {
      length[0] = (byte)(len>>8);
      length[1] = (byte)(len&256);
    }

    public void setTimestamp(){

    }

    public void setCRC1(int crc) {
      crc1[0] = (byte)(crc>>24);
      crc1[1] = (byte)(crc>>16);
      crc1[2] = (byte)(crc>>8);
      crc1[3] = (byte)(crc&256);
    }

    public void setCRC2(int crc) {
      crc2[0] = (byte)(crc>>24);
      crc2[1] = (byte)(crc>>16);
      crc2[2] = (byte)(crc>>8);
      crc2[3] = (byte)(crc&256);
    }

    public void setPayload(String pay)
    throws UnsupportedEncodingException {
      payload = pay.getBytes("IBM01140");
    }
}
