package liamcole;

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

    public static void main(String[] args) {
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
     * clientSide: Sockets
     */
    public static void clientSide(String address, int port, String fileName, String message) {

        DatagramSocket clientSock = null;
        DatagramPacket sendData = null;
        InetAddress addressInet = null;
        byte[] write = new byte[65536];
        write = message.getBytes();// we can assume input or file is already been read

        // InetAddress conversions
        try {
            addressInet = InetAddress.getByName(address);
        } catch (UnknownHostException uh) {
            uh.printStackTrace();
        }

        // Sockets
        try {
            clientSock = new DatagramSocket();// doesnt require port at this time but packet does
            sendData = new DatagramPacket(write, write.length, addressInet, port);
            clientSock.send(sendData);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
