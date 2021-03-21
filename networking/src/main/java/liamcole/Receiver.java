package liamcole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
    CPSC 3780 Cole Anderson and Liam King
    Checkpoint1 (RECEIVER)
*/

public class Receiver {
    public static void main(String[] args) {
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
            } else if (args.length > 2) {
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
    public static void serverSide(int port, String fileName) {

        // Initializations
        DatagramSocket serverSock = null;
        DatagramPacket receivedData = null;
        byte[] buff = new byte[65536];

        try {
            // Received and Translate information
            serverSock = new DatagramSocket(port);
            receivedData = new DatagramPacket(buff, buff.length);
            serverSock.receive(receivedData);
            byte[] read = receivedData.getData();
            String output = new String(read, 0, receivedData.getLength());

            // if not file in command line args
            if (fileName == "") {
                System.out.println(output);
            }
            // else file is specified in command line args
            else {
                writeFile(fileName, output);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
