// package liamcole; //(because why cant java just work right)

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    CPSC 3780 Cole Anderson and Liam King
    Checkpoint1 (RECEIVER)
*/

public class Receiver {
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
        serverSide(port, file);

        System.exit(0);// temporary

    }// ENDMAIN

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName, String write) {
        System.out.println("DEBUG1:" + write);
        // CREATE FILE OR ALERT OF OVERWRITE
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile()) {

            } else {
                System.out.println("FILE EXISTS. OVERWRITING");// FIX LATER
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

    /**
     * serverSide: Sockets
     */
    public static void serverSide(int port, String fileName) throws Exception {

        // Initializations
        DatagramSocket serverSock = null;
        DatagramPacket receivedData = null;
        byte[] buff = new byte[12 + 512 + 4]; // First 12 bytes top header, 512 max payload, 4 bytes CRC2

        try {
            // Socket OPEN:
            serverSock = new DatagramSocket(port);
            // while (true) {
            // System.out.print("Waiting on packet...");

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
            r.setLength(read[2] + read[3]);
            r.setTimestamp(read[4] + read[5] + read[6] + read[7]);
            r.setCRC1();

            byte[] temp = new byte[r.getLength()];
            for (int i = 0; i < r.getLength(); i++) {
                temp[i] = read[12 + i];
            }
            String p = new String(temp);
            r.setPayload(p);

            String output = r.getPayload();

            // if not file in command line args
            if (fileName == "") {
                System.out.println(output);
            }
            // else file is specified in command line args
            else {
                writeFile(fileName, output);
            }

            /// 3) Create/Set acknowledgment packet:
            Header reply = new Header();

            if (r.getTR() == 1) { // if TR is 1 we send a NACK else we send ACK
                reply.setType(0xD5); // 11010101
                reply.setTR(0xD5);
                reply.setWindow(0xD5);
            } else {
                reply.setType(0x95); // 10010101
                reply.setTR(0x95);
                reply.setWindow(0x95);
            }
            reply.setSeqnum(0);
            reply.setLength(0);

            /// 4) Sends acknowledgement packet:
            try {
                InetAddress from = receivedData.getAddress();
                // get address that data was received from so we know where to send
                // acknowledgement
                DatagramPacket ack = new DatagramPacket(reply.ackknowledgement(), reply.ackknowledgement().length, from,
                        port);
                // create packet for ackknowledgement
                serverSock.send(ack); // send acknowledgement back to sender

            } catch (IOException io) {
                io.printStackTrace();
            }
            receivedData = null;
            // } // ends the while loop

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * lazy print function:
     * 
     * @param statement
     */
    public static void print(String statement) {
        System.out.println(statement);
    }
}
