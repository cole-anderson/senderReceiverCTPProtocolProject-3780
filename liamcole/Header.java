// package liamcole; //(because why cant java just work right)

import java.util.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.rmi.UnexpectedException;
import java.io.UnsupportedEncodingException;

public class Header {
  Packet p = new Packet(); // packet

  void setTestB(int val) {
    p.testB = (byte) val;
    System.out.println("/////" + p.testB);
  }

  // 32 bits == 4 bytes & 4096 bits = 512 bytes

  // (4 bytes) Type|TR|Window|Seqnum|Length
  // (4 bytes) Timestamp
  // (4 bytes) CRC1
  // (up to 512 bytes) Payload
  // (4 bytes) CRC2

  // enum Type {
  // DATA, ACK, NACK
  // }

  // Constructor:
  public Header() {
    // NULL
  }

  // ********************************************************
  /*
   * Type Field Setter&Getter: (TODO: DONE)
   */
  public void setType(int val) throws Exception {
    byte temp = (byte) (val >> 6);
    if (temp != 0) {
      p.type = temp;
    } else {
      throw new Exception("type invalid"); // fix later
    }
  }

  public byte getType() {
    return p.type;
  }

  // ********************************************************
  /*
   * TR Field Setter&Getter: (TODO: DONE)
   */
  public void setTR(int val) {
    p.tr = (byte) (val >>> 5 % 2);
  }

  public byte getTR() {
    return p.tr;
  }

  // ********************************************************
  public byte getWindow() {
    return p.window;
  }

  public void setWindow(int val) {
    p.window = (byte) (val & 31);
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

  public void setSeqnum(int seq) {
    p.seqnum = (byte) seq;
  }

  // ********************************************************
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

  // ********************************************************
  public void setTimestamp() {

  }

  // public void setCRC1(int crc) {
  // p.crc1[0] = (byte) (crc >> 24);
  // crc1[1] = (byte) (crc >> 16);
  // crc1[2] = (byte) (crc >> 8);
  // crc1[3] = (byte) (crc & 255);
  // }

  // public void setCRC2(int crc) {
  // crc2[0] = (byte) (crc >> 24);
  // crc2[1] = (byte) (crc >> 16);
  // crc2[2] = (byte) (crc >> 8);
  // crc2[3] = (byte) (crc & 255);
  // }

  // public void setPayload(String pay) throws UnsupportedEncodingException {
  // payload = pay.getBytes("IBM01140");
  // }
}
