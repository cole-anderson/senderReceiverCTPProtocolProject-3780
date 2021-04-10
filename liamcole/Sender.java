import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Scanner;

import java.lang.Runnable;

/**
 * @author: Cole Anderson & Liam King. CPSC3780
 */

public class Sender {
    Boolean running = true;
    DatagramSocket clientSock = null;
    DatagramPacket data = null;
    InetAddress addressInet = null;
    Boolean[] seqArray = new Boolean[255];
    int winSize = 1;

    public class SenderThread implements Runnable {
        int seqnum;
        byte[] mb;
        int portp;
        DatagramPacket send;

        public SenderThread(int seq, DatagramPacket s) {
            seqnum = seq;
            send = s;
        }

        public void run() {
            DatagramPacket data = null;

            // ACK&NACK INITIALIZATION:
            byte[] recBuf = new byte[1]; // fixme:
            DatagramPacket acknowledgement;
            acknowledgement = new DatagramPacket(recBuf, recBuf.length);
            // Set Packet Parameters
            /*
             * TOCONSIDER: TR SHOULD ALWAYS BE 0
             * 
             * -> 0x48 01 0 01000 -> (base case for now)
             * 
             * ->
             * 
             * (0) TYPE|TR|WINDOW
             * 
             * Type 1: Data Type 2: ACK(NOT REALLY APPLICABLE SENDER SIDE) Type 3: NACK
             * 
             * TR: Default 0 (will be set eventally by linksim)
             * 
             * Window: TODO: THIS HERE YES
             * 
             * (1) SeqNum TODO: THIS HERE YES
             * 
             * (2)(3) Length
             * 
             * (4-5-6-7) Timestamp TODO: THUS HERE YES
             * 
             * (8-9-10-11) CRC1
             * 
             * (12 to Length) Payload
             * 
             * (if applicable CRC2)
             */

            /**
             * DATAGRAMPACKET TRY CATCH:
             */
            try {

                data = send;
                System.out.println("==Sending Packet==\n");
                clientSock.send(data);

                // System.out.println("==Waiting for 2 sec==");
                Thread.sleep(2000);
                System.out.println("==Done waiting==");

                clientSock.receive(acknowledgement);
                System.out.println("==Recieved Packet Back==\n");

                byte[] readack = acknowledgement.getData();

                Header a = new Header();
                a.setType(readack[0]);
                a.setTR(readack[0]);
                a.setWindow(readack[0]);
                if (a.getType() == (byte) 2) {
                    System.out.println("[ACK RECEIVED] SEQNUM: " + seqnum + "\n");
                    seqArray[seqnum] = true;
                    winSize = a.getWindow();
                } else if (a.getType() == (byte) 3)
                    System.out.println("[NACK RECEIVED]\n");
            } catch (IOException io) {
                io.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {
        /*
         * sender -f data.txt 127.0.0.1 64341 [-f, file, ipaddress, port] ........
         * sender 127.0.0.1 64341 [ipaddress,port]
         */
        String file = "";
        int port = 0;
        String address = "";
        String message = "";
        Boolean userMode = false;

        // If command line argument does not contain a host and port(exit)
        if (args.length <= 1) {
            System.out.println("No Port Specified, Exiting Program");
            System.exit(0);
        }
        // Sender -f data.txt 127.0.0.1 63431
        else if (args[0].equals("-f") && args.length == 4) {
            System.out.println("Data file selected for transmission: " + args[1]);
            file = args[1];
            address = args[2];
            port = Integer.parseInt(args[3]);
            message = readFile(file);
        }
        // Sender 127.0.0.1 64341
        else if (args.length == 2) {
            System.out.println("Message mode, no file specified. Type <end> to stop sending messages");
            address = args[0];
            port = Integer.parseInt(args[1]);
            message = "";
            userMode = true;

        }
        // Socket Connecting
        Sender send = new Sender();
        send.clientSide(address, port, file, message, userMode);
    }

    /*
     * readFile: reads a textfile into an object
     */
    static String readFile(String fileName) {
        String read = "";
        // Checks for existing file to read from
        try {
            File myFile = new File(fileName);
            Scanner reader = new Scanner(myFile);
            while (reader.hasNextLine()) {
                read = reader.nextLine();
            }
            reader.close();
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            System.out.println("File not found exiting program");
            System.exit(0);
        }
        return read;
    }

    /*
     * Allows for non prompted user input in the case a file is not specified to be
     * sent
     */

    static String messageMode() {
        String input = "";
        // System.out.println("Enter your message");// DEL: DELETE THIS LINE
        Scanner inline = new Scanner(System.in);
        input = inline.nextLine();
        if (input.equals("end")) {

            inline.close();
            return "";
        } else
            return input;
    }

    /**
     * Generates timestamp
     * 
     * @return time
     */
    static int generateTime() {
        int time = (int) (new Date(System.currentTimeMillis()).getTime() / 1000);
        System.out.println("//debug generateTime:" + time);
        return time;
    }

    /**
     * Creates the main buffer from the input file or text
     * 
     * @param input
     * @return buffer
     */
    public static byte[] createBuffer(String input) {
        byte[] buffer = input.getBytes();

        return buffer;
    }

    public byte[] subArray(byte[] b, int index) {
        if (b.length < 512) {
            return b;
        }
        if (index * 512 + 511 > b.length) {
            byte[] temp = Arrays.copyOfRange(b, index * 512, b.length);
            return temp;
        }
        byte[] temp = Arrays.copyOfRange(b, index * 512, index * 512 + 512);
        return temp;
    }

    /**
     * awdawdasd clientSide: Sockets
     * 
     * @throws Exception
     */
    public void clientSide(String address, int port, String fileName, String message, Boolean usermode)
            throws Exception {

        // INITIALIZATIONS:
        Header headerOne = new Header();
        int seq = 0;
        Arrays.fill(seqArray, Boolean.FALSE);

        // BUFFER INITIALIZATION(FROM MESSAGE)
        byte[] messageBuffer = createBuffer(message);

        // SOCKET+INETADDRESS INITIALIZATION:
        /**
         * Following try-catch allows for both peer to peer(different ip address's) and
         * localhost communication to be enabled without errors ending the program
         */
        try {
            clientSock = new DatagramSocket(port);
            System.out.println("--PEER TO PEER MODE ENABLED--");
        } catch (BindException be) {
            System.out.println(be);
            System.out.println("--LOCALHOST MODE ENABLED--");
            clientSock = new DatagramSocket();
        }
        /**
         * Convert ip Address to inet address for use in datagram sockets
         */
        try {
            addressInet = InetAddress.getByName(address);
        } catch (UnknownHostException uh) {
            uh.printStackTrace();
        }

        int lastseq = 0;
        // start primay loop
        while (running == true) {

            // Setting Header Parameters:
            for (int i = 0; i < 255; i++) {
                if (seqArray[i] == false) {
                    seq = i;
                    break;
                }
            }
            for (int i = seq + 1; i < 255; i++) {
                seqArray[i] = false;
            }
            for (int i = 0; i < winSize; i++) {
                // Allows for continuous messaging to be enabled
                if (usermode == true) {
                    message = messageMode(); // FIXME: HERE
                    if (message == "") {
                        usermode = false;
                        break;
                    }
                    messageBuffer = createBuffer(message);
                }

                System.out.println("==Preparing Packet==\n");
                headerOne.setType(0x41);
                headerOne.setTR(0x41);
                headerOne.setWindow(winSize);
                headerOne.setSeqnum(seq);
                byte[] temp = subArray(messageBuffer, seq);

                if (temp.length < 511) {

                    // Allows for continuous user input
                    if (usermode == true) {
                        headerOne.setLength(temp.length);// if current payload is less then max 512 bytes

                    } else {
                        headerOne.setLength(temp.length);// if current payload is less then max 512 bytes
                        running = false;// will end loop after this transmission due to no more file to read
                        lastseq = seq;
                        System.out.println("FINAL SEQNUM IS: " + lastseq);
                    }

                    headerOne.setLength(temp.length);// if current payload is less then max 512 bytes
                } else {
                    headerOne.setLength(512);// if current payload is more than max 512 bytes
                }
                headerOne.setTimestamp(generateTime());
                headerOne.setCRC1();
                headerOne.setPayload(temp);
                headerOne.setCRC2();

                // Preparing Packet for Transmission:
                byte[] write = headerOne.returnCTPByteArray();

                DatagramPacket data = new DatagramPacket(write, write.length, addressInet, port);
                SenderThread t = new SenderThread(seq, data);
                Thread t1 = new Thread(t);
                t1.start();
                seq++;
                if (running == false) {
                    System.out.println("WAITING FOR LAST THREAD");
                    try {
                        t1.join();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (seqArray[lastseq] != true) {
                        running = true;
                    } else {
                        break;
                    }
                }

            }

            Thread.sleep(2500);

        } // END PRIMARY WHILE
        try {
            // FOLLOWING SENDS EMPTY PACKET TO END RECEIVER (DOES NOT ACCOUNT FOR THREADS)
            Header emptyEnd = new Header();
            byte[] recBuf = new byte[1]; // fixme:
            DatagramPacket acknowledgement;
            acknowledgement = new DatagramPacket(recBuf, recBuf.length);
            emptyEnd.setType(0x41);
            emptyEnd.setTR(0x41);
            emptyEnd.setWindow(0x41);
            emptyEnd.setSeqnum(seq);
            emptyEnd.setLength(0);
            emptyEnd.setTimestamp(55);
            emptyEnd.setCRC1();

            byte[] finalPacket = emptyEnd.ackknowledgement();
            DatagramPacket end = new DatagramPacket(finalPacket, finalPacket.length, addressInet, port);
            clientSock.send(end);
            clientSock.receive(acknowledgement);
            System.out.println("==Recieved final ack==");
            clientSock.close();// CLOSE SOCKET
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
