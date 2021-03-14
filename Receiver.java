import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
// import java.net.DatagramPacket;
// import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
// import java.net.SocketException;

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
        if (args[0].equals("-f") && args.length == 2) {
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
        System.out.println("///PORT  " + port);
        serverSide(port, file);

    }// ENDMAIN

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName, DataOutputStream write) {
        // TODO: writefile
    }

    public static void serverSide(int port, String fileName) {

        // TCP BASED IMPLEMENTATION:
        try {
            Socket acceptSocket = null;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Receiver Server Active...Waiting For Client To Send");
            acceptSocket = serverSocket.accept();
            System.out.println("//Connection Successful");

            // Accept information from sender
            InputStream receivedData = acceptSocket.getInputStream();
            DataInputStream dataIn = new DataInputStream(receivedData);

            // Read into a String
            String trans = dataIn.readUTF();

            // Output to command line transmitted message
            System.out.println(trans);

            // Cleanup
            acceptSocket.close();
            serverSocket.close();

        } catch (IOException io) {
            System.out.println(io);
        }

    }
}
