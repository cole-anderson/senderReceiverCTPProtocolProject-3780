// package liamcole; //(because why cant java just work right)

import java.util.Arrays;
import java.util.zip.CRC32;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
// import java.nio.charset.Charset;
// import java.rmi.UnexpectedException;
import java.io.UnsupportedEncodingException;

public class Header {
  // Packet obj that contains the given fields
  Packet p = new Packet(); // packet

  /**
   * 
   * @return Packet
   */
  public Packet retPacket() {
    return p;
  }

  /**
   * 
   * @return packet for just acknowledgement
   * @throws IOException
   */
  public byte[] ackknowledgement() throws IOException {
    byte[] acknowledgementP = null;

    ByteArrayOutputStream temp = new ByteArrayOutputStream();

    byte tempType = (byte) ((byte) this.getType() << 6); // WORKS!
    byte tempTr = this.getTR();
    byte tempWindow = this.getWindow();
    byte[] addTo = new byte[] { (byte) (tempType + tempTr + tempWindow) };

    temp.write(addTo);
    temp.write(p.seqnum); // 2 byte
    temp.write(p.length); // 2 byte
    temp.write(p.timestamp); // 4 byte
    temp.write(p.crc1);

    acknowledgementP = temp.toByteArray();

    return acknowledgementP;
  }

  /**
   * 
   * @return packet as a byte[] for use in transmission over datagram socket
   * @throws IOException
   */
  public byte[] returnCTPByteArray() throws IOException {
    byte[] retValue = null;

    ByteArrayOutputStream temp = new ByteArrayOutputStream();

    byte tempType = (byte) ((byte) this.getType() << 6); // WORKS!
    byte tempTr = this.getTR();
    byte tempWindow = this.getWindow();

    byte[] addTo = new byte[] { (byte) (tempType + tempTr + tempWindow) }; //

    temp.write(addTo); // 1 byte
    temp.write(p.seqnum); // 1 byte
    temp.write(p.length); // 2 byte
    temp.write(p.timestamp); // 4 byte
    temp.write(p.crc1); // 4 byte
    temp.write(p.payload); // 0-512 bytes
    if (p.crc2 != null) {
      // Accounts for optional crc2
      temp.write(p.crc2);
    }
    retValue = temp.toByteArray();
    return retValue;
  }

  // 32 bits == 4 bytes & 4096 bits = 512 bytes

  // (4 bytes) Type|TR|Window|Seqnum|Length
  // (4 bytes) Timestamp
  // (4 bytes) CRC1
  // (up to 512 bytes) Payload
  // (4 bytes) CRC2

  // Constructor:
  public Header() {
    // NULL
  }

  // ********************************************************
  /*
   * Type Field Setter&Getter:
   */
  public void setType(int val) {
    byte temp = (byte) (val >> 6);
    if (temp != 0) {
      p.type = (byte) Math.abs(temp);
    } else {
      // throw new Exception("type invalid setType"); // fix later
    }
  }

  public byte getType() {
    return p.type;
  }

  // ********************************************************
  /*
   * TR Field Setter&Getter:
   */
  public void setTR(int val) {
    p.tr = (byte) (val >>> 5 & 1);
  }

  public byte getTR() {
    return p.tr;
  }

  // ********************************************************
  /*
   * Window Field Setter&Getter:
   */
  public void setWindow(int val) {
    // System.out.println("window" + val);
    // System.out.println("window after" + (byte) (val & 31));
    p.window = (byte) (val & 31);
  }

  public byte getWindow() {
    return p.window;
  }

  // ********************************************************
  /*
   * TSeqnum Field Setter&Getter:
   */
  public byte getSeqnum() {
    return p.seqnum;
  }

  public void setSeqnum(int seq) {
    p.seqnum = (byte) seq;
  }

  // ********************************************************
  /*
   * Length Field Setter&Getter:
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
  /*
   * TimeStamp Field Setter&Getter:
   */
  public void setTimestamp(int val) {
    p.timestamp[0] = (byte) (val >> 24 & 0xFF);
    p.timestamp[1] = (byte) (val >> 16 & 0xFF);
    p.timestamp[2] = (byte) (val >> 8 & 0xFF);
    p.timestamp[3] = (byte) (val & 255 & 0xFF);
  }

  public int getTimestamp() {
    int convertedTimestamp = ByteBuffer.wrap(p.timestamp).getInt();
    return convertedTimestamp;
  }

  // ********************************************************
  /*
   * CRC1 Field Setter&Getter:
   */
  public void setCRC1() throws IOException {
    CRC32 c = new CRC32();
    ByteArrayOutputStream temp = new ByteArrayOutputStream();

    byte tempType = (byte) ((byte) this.getType() << 6); // WORKS!
    byte tempTr = this.getTR();
    byte tempWindow = this.getWindow();
    byte[] addTo = new byte[] { (byte) (tempType + tempTr + tempWindow) };
    temp.write(addTo);
    temp.write(p.seqnum); // 2byte
    temp.write(p.length); // 2 byte
    temp.write(p.timestamp); // 4 byte

    c.update(temp.toByteArray());

    ByteBuffer b = ByteBuffer.allocate(4);
    b.putInt((int) c.getValue());
    p.crc1 = b.array();
  }

  public int getCRC1() {
    int convertedCRC1 = ByteBuffer.wrap(p.crc1).getInt();
    return convertedCRC1;
  }

  // ********************************************************
  /*
   * CRC2 Field Setter&Getter:
   */
  public void setCRC2() {
    CRC32 c = new CRC32();
    c.update(p.payload);

    ByteBuffer b = ByteBuffer.allocate(4);
    b.putInt((int) c.getValue());
    p.crc2 = b.array();
  }

  public int getCRC2() {
    int convertedCRC2 = ByteBuffer.wrap(p.crc2).getInt();
    return convertedCRC2;
  }

  // ********************************************************
  /*
   * Payload Field Setter&Getter:
   * 
   * This function both sets payload and edits the current message buffer in such
   * a way that allows for multiple packets to be created when the buffer is
   * larger than 512 bytes
   */
  public byte[] setPayload(byte[] buffer) throws UnsupportedEncodingException {
    byte[] nextBuffer = null;

    if (this.getTR() == 0) {
      /**
       * (1) If buffer < 512 last packet
       * 
       * If buffer >=512 still need more packets after current
       */
      if (buffer.length < 512) {
        p.payload = buffer;
        nextBuffer = null; // final packet
      } else if (buffer.length >= 512) {
        p.payload = Arrays.copyOfRange(buffer, 0, 512);
        nextBuffer = Arrays.copyOfRange(buffer, 512, buffer.length);
      }

    } else {
      p.payload = null;
      nextBuffer = buffer;
    }
    return nextBuffer;
  }

  public String getPayload() {
    String str = new String(p.payload);
    return str;
  }

}
