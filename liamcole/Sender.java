import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author: Cole Anderson & Liam King. CPSC3780
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
        byte[] recBuf = new byte[1];
        DatagramPacket acknowledgement;
        acknowledgement = new DatagramPacket(recBuf, recBuf.length);

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
         * ->
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
        headerOne.setType(0x41);
        headerOne.setTR(0x41);
        headerOne.setWindow(0x41);
        headerOne.setSeqnum(0); // need to do calculations still
        headerOne.setLength(message.length()); // do error check for if over 512 convert to multiple packets
        headerOne.setTimestamp(55); // need to do creation still
        headerOne.setCRC1();// need to do calculations still
        headerOne.setPayload(message);
        // CRC2 TODO: COLE
        headerOne.setCRC2();

        // Preparing Packet for Transmission:
        byte[] write = headerOne.returnCTPByteArray();

        /**
         * Convert ip Address to inet address for use in datagram sockets
         */
        try {
            addressInet = InetAddress.getByName(address);
        } catch (UnknownHostException uh) {
            uh.printStackTrace();
        }

        /**
         * Socket try-catch
         */
        try {
            /**
             * Following try-catch allows for both peer to peer(different ip address's) and
             * localhost communication to be enabled without errors ending the program
             */
            // clientSock = new DatagramSocket();
            try {
                clientSock = new DatagramSocket(port);
                System.out.println("--PEER TO PEER MODE ENABLED--");
            } catch (BindException be) {
                System.out.println(be);
                System.out.println("--LOCALHOST MODE ENABLED--");
                clientSock = new DatagramSocket();
            }

            data = new DatagramPacket(write, write.length, addressInet, port);
            clientSock.send(data);
            System.out.println("Waiting for 2 sec");
            Thread.sleep(2000);
            System.out.println("Done waiting");

            clientSock.receive(acknowledgement);
            System.out.println("Recieved ack");
            byte[] readack = acknowledgement.getData();
            System.out.println("First byte of ack: " + readack[0]);
            Header a = new Header();
            byte b = readack[0];
            System.out.println(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
            a.setType(readack[0]);
            a.setTR(readack[0]);
            a.setWindow(readack[0]);
            System.out.println("our ack is" + a.getType());

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
