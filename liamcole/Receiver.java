import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.io.ByteArrayOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Date;
import java.lang.Runnable;

/**
 * @author: Cole Anderson & Liam King. CPSC3780
 */
public class Receiver {
    DatagramSocket serverSock = null;
    Boolean running = true;
    byte[][] pay = new byte[256][512];
    int lastseq = 0;
    int lasttime = 0;

    public class ReceiverThread implements Runnable {
        DatagramPacket receivedData = null;
        byte[] buff = new byte[12 + 512 + 4]; // First 12 bytes top header, 512 max payload, 4 bytes CRC2
        public ReceiverThread() {
        }
        public void run() {
            try {
                /// 1) Receives packet:
                receivedData = new DatagramPacket(buff, buff.length);
                serverSock.receive(receivedData);
                byte[] read = receivedData.getData();

                /// 2) Re-Builds header-info in packet using setters:
                Header r = new Header();
                r.setType((int) read[0]);
                r.setTR((int) read[0]);
                r.setWindow((int) read[0]);

                r.setSeqnum(read[1]);

                byte[] templen = new byte[2];
                templen[0] = read[2];
                templen[1] = read[3];
                ByteBuffer bb = ByteBuffer.wrap(templen);
                bb.order(ByteOrder.BIG_ENDIAN);
                Short len = bb.getShort();
                r.setLength(len);

                byte[] temptime = new byte[4];
                temptime[0] = read[4];
                temptime[1] = read[5];
                temptime[2] = read[6];
                temptime[3] = read[7];
                ByteBuffer bbt = ByteBuffer.wrap(temptime);
                bbt.order(ByteOrder.BIG_ENDIAN);
                int timestamp = bbt.getInt();
                lasttime = timestamp;
                r.setTimestamp(lasttime);
                r.setCRC1();

                byte[] temp = new byte[len];
                for (int i = 0; i < r.getLength(); i++) {
                    temp[i] = read[12 + i];
                }
                r.setPayload(temp);
                // Check for if payload exists or if NACK / FINAL ACK
                if (!r.getPayload().equals("")) {
                    System.out.println("**PAYLOAD RECEIVED**: " + r.getPayload());
                    pay[r.getSeqnum()] = r.p.payload;
                } else {
                    running = false;
                    lastseq = r.getSeqnum();
                }


                    

                /// 3) Create/Set acknowledgment packet:
                Header reply = new Header();
                if (r.getTR() == 1) { // if TR is 1 we send a NACK else we send ACK
                    reply.setType(0xDF); // 11000001
                    reply.setTR(0xDF);
                    reply.setWindow(0xDF);
                    reply.setSeqnum(r.getSeqnum());
                } else {
                    reply.setType(0x9F); // 10000001
                    reply.setTR(0x9F);
                    reply.setWindow(0x9F);
                    reply.setSeqnum(r.getSeqnum() + 1);
                }
                reply.setTimestamp(generateTime());
                reply.setLength(0);
                reply.setCRC1();

                /// 4) Sends acknowledgement packet:
                try {
                    // Sets the acknowledgment packet to send back to sender(ip + port)
                    InetAddress from = receivedData.getAddress();
                    int recPort = receivedData.getPort(); // accounts for if localhost

                    DatagramPacket ack = new DatagramPacket(reply.ackknowledgement(), reply.ackknowledgement().length,
                            from, recPort);

                    System.out.println("SENDING ACK#: " + r.getSeqnum());
                    serverSock.send(ack); // send acknowledgement back to sender

                } catch (IOException io) {
                    io.printStackTrace();
                }
                receivedData = null;

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        /*
         * Receiver -f data-received.txt 64341 [OR] Receiver 64341
         */
        String file = "";
        int port = 0;

        // (1)(EXAMPLE INPUT) Receiver -f data_received.txt 6431
        if (args[0].equals("-f") && args.length == 3) {
            System.out.println("Data file selected for receiving data: " + args[1]);
            file = args[1];
            port = Integer.parseInt(args[2]);
        }
        // (2)(EXAMPLE INPUT) Receiver 64341
        else if (args.length == 1) {
            System.out.println("No data file selected for receiving data. Outputting to terminal");
            port = Integer.parseInt(args[0]);
        }
        // (3)(SHOULD ACCOUNT FOR ALL INVALID INPUT)
        else {
            if (args.length == 0) {
                System.out.println("No port specified. Exiting Program");
                System.exit(0);
            } else if (args.length >= 2) {
                System.out.println("Incorrect Commandline Input. Exiting Program");
                System.exit(0);
            }
        }
        // Socket Function:
        Receiver rec = new Receiver();
        rec.serverSide(port, file);

        System.exit(0);// temporary

    }// ENDMAIN

    /**
     * Generates timestamp
     * 
     * @return time
     */
    static int generateTime() {
        int time = (int) (new Date(System.currentTimeMillis()).getTime() / 1000);
        // System.out.println("///debug generateTime:" + time);
        return time;
    }

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName, String write) {
        // CREATE FILE OR ALERT OF OVERWRITE
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile()) {

            } else {
                System.out.println("--FILE EXISTS OVERWRITING--");
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        // WRITES TO TEXTFILE
        try {
            FileWriter writeToFile = new FileWriter(fileName);
            writeToFile.write(write);// write string into file
            writeToFile.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public ByteArrayOutputStream reconstructPayload() {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        try {
            for(int i = 0; i < lastseq; i++) {
                temp.write(pay[i]);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return temp;
    }

    /**
     * serverSide: Sockets
     */
    public void serverSide(int port, String fileName) throws Exception {

        // Initializations
        try {
            serverSock = new DatagramSocket(port);
        } catch (IOException io) {
            io.printStackTrace();
        }
        ThreadGroup recs = new ThreadGroup("Rec");
        while(running == true) {
            if(recs.activeCount() < 32) {
            ReceiverThread t = new ReceiverThread();
            Thread t1 = new Thread(recs, t);
            t1.start();
            }
        }
        Thread.sleep(1500);
        // int act = recs.activeCount() * 2;
        // Thread[] threads = new Thread[act];
        // recs.enumerate(threads);
        // for(int i = 0; i < act; i++) {
        //     Thread t = threads[i];
        //     if(t != null)
        //         t.join();
        // }
        ByteArrayOutputStream payl = reconstructPayload();

        // if not file in command line args
        if (fileName == "") {
            System.out.println("PRINTING MESSAGELOG: " + payl.toString());
        }
        // else file is specified in command line args
        else {
            System.out.println("WRITING MESSAGELOG TO FILE...");
            writeFile(fileName, payl.toString());
        }
        // serverSock.close();
        // System.exit(0);
    }
}
