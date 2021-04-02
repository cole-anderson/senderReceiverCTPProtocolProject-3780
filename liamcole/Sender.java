// package liamcole; //(because why cant java just work right)

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/*
    CPSC 3780 Cole Anderson and Liam King
    Checkpoint1 (SENDER)
*/

public class Sender {

    public static void main(String[] args) throws Exception {
        /*
         * sender -f data.txt 127.0.0.1 64341 [-f, file, ipaddress, port] ........
         * sender 127.0.0.1 64341 [ipaddress,port]
         */
        String file = "";
        int port = 0;
        String address = "";
        String message = "";

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
            System.out.println("Message mode, no file specified");
            address = args[0];
            port = Integer.parseInt(args[1]);
            message = messageMode();

        }
        // Socket Connecting
        clientSide(address, port, file, message);
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
        inline.close();
        return input;
    }

    /**
     * awdawdasd clientSide: Sockets
     * 
     * @throws Exception
     */
    public static void clientSide(String address, int port, String fileName, String message) throws Exception {

        DatagramSocket clientSock = null;
        DatagramPacket data = null;
        InetAddress addressInet = null;

        // For ACK.NACK:
        byte[] recBuf;
        DatagramPacket acknowledgement;

        /**
         * Checkpoint 3: TODO: COLE
         */
        // Packet creation (TODO: )
        // CONTEXT: Header contains the packet, Packet contains the actual fields
        Header headerOne = new Header();

        // Set Packet Parameters
        /*
         * TOCONSIDER: TR SHOULD ALWAYS BE 0
         * 
         * -> 0x48 01 0 01000 -> (base case for now)
         * 
         * (0) TYPE|TR|WINDOW
         * 
         * Type 1: Data Type 2: ACK(NOT REALLY APPLICABLE SENDER SIDE) Type 3: NACK
         * 
         * TR: Default 0 (will be set eventally by linksim)
         * 
         * Window: TODO: THIS HERE LOL
         * 
         * (1) SeqNum
         * 
         * (2)(3) Length
         * 
         * (4-5-6-7) Timestamp
         * 
         * (8-9-10-11) CRC1
         * 
         * (12 to Length) Payload
         * 
         * (if applicable CRC2)
         */

        // Setting Header Parameters:
        headerOne.setType(0x48);
        headerOne.setTR(0x48);
        headerOne.setWindow(0x48);
        headerOne.setSeqnum(0); // need to do calculations still
        headerOne.setLength(message.length()); // do error check for if over 512 convert to multiple packets
        headerOne.setTimestamp(55); // need to do creation still
        long lol = 3175023593L;
        int v = (int) lol;
        headerOne.setCRC1(v);// need to do calculations still
        headerOne.setPayload(message);
        // CRC2 TODO: COLE
        headerOne.setCRC2(0xCBF43926);

        // Preparing Packet for Transmission:
        byte[] write = headerOne.returnCTPByteArray();
        byte[] read = null;

        // read

        // InetAddress conversions
        try {
            addressInet = InetAddress.getByName(address);
        } catch (UnknownHostException uh) {
            uh.printStackTrace();
        }

        // Sockets
        try {
            clientSock = new DatagramSocket();// doesnt require port at this time but packet does
            data = new DatagramPacket(write, write.length, addressInet, port);
            clientSock.send(data);

            // while (true) {
            // if (true) {
            // clientSock.receive(rec);
            // System.out.println("//" + rec.getData());

            // }

            // }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
