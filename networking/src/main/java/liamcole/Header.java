package liamcole;

import java.util.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

public class Header {
  Packet p = new Packet();

  // 32 bits == 4 bytes & 4096 bits = 512 bytes

  // (4 bytes) Type|TR|Window|Seqnum|Length
  // (4 bytes) Timestamp
  // (4 bytes) CRC1
  // (up to 512 bytes) Payload
  // (4 bytes) CRC2

  enum Type {
    DATA, ACK, NACK
  }

  // Constructor: (TODO: HERE)
  public Header() {
  }

  /**
   * Getters:
   */

  public BitSet getType() {
    return p.type;
  }

  public BitSet getTR() {
    return p.tr;
  }

  public BitSet getWindow() {
    return p.window;
  }

  public byte getSeqnum() {
    return p.seqnum;
  }

  public byte[] getTimeStamp() {
    return p.timestamp;
  }

  public byte[] getCRC1() {
    return p.crc1;
  }

  public byte[] getCRC2() {
    return p.crc2;
  }

  public String getPayload() {
    String str = new String(p.payload);
    return str;
  }

  /**
   * Setters:
   */
  public void setType(Type types) {
    switch (types) {
    case DATA:
      p.type.set(1);
      break;
    case ACK:
      p.type.set(0);
      break;
    case NACK:
      p.type.set(1);
      p.type.set(0);
      break;
    }
  }

  public void setTR() {
    tr.set(0);
  }

  public void setWindow(int val) {
    int i = 0;
    while (val != 0) {
      if (val % 2 != 0) {
        P.window.set(i);
      }
      ++i;
      val = val >> 1;
    }
  }

  public void setSeqnum(int seq) {
    p.seqnum = (byte) seq;
  }

  // ************************************ */
  /*
   * Length Field Setter&Getter: (TODO: DONE)
   */
  public void setLength(int len) {
    p.length[0] = (byte) (len >>> 8);
    p.length[1] = (byte) (len);
  }

  public short getLength() {
    short convertedLength = ByteBuffer.wrap(p.length).getShort();
    return convertedLength;
  }

  // ************************************ */
  public void setTimestamp() {

  }

  public void setCRC1(int crc) {
    p.crc1[0] = (byte) (crc >> 24);
    crc1[1] = (byte) (crc >> 16);
    crc1[2] = (byte) (crc >> 8);
    crc1[3] = (byte) (crc & 256);
  }

  public void setCRC2(int crc) {
    crc2[0] = (byte) (crc >> 24);
    crc2[1] = (byte) (crc >> 16);
    crc2[2] = (byte) (crc >> 8);
    crc2[3] = (byte) (crc & 256);
  }

  public void setPayload(String pay) throws UnsupportedEncodingException {
    payload = pay.getBytes("IBM01140");
  }
}
