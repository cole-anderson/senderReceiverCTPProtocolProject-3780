
//Client Side
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Receiver {
    public static void main(String[] args) {
        /*
         * Receiver -f data-received.txt 64341 [OR] Receiver 64341
         */
        String file = "";
        int port;

        // (1)(EXAMPLE INPUT) Receiver -f data_received.txt 6431
        if (args[0].equals("-f") && args.length == 2) {
            System.out.println("Data file selected for receiving data: " + args[1]);
            file = args[1];
            port = Integer.parseInt(args[2]);
        }
        // (2)(EXAMPLE INPUT) Receiver 64341
        else if (args.length == 1) {
            System.out.println("No data file selected for receiving data. Outputting to terminal");
            port = Integer.parseInt(args[1]);
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

    }// ENDMAIN

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName, DataOutputStream write) {
        // TODO: writefile
    }

    public static void clientConnect(String address, int port, String fileName) {
        try {
            Socket clientSocket = new Socket(address, port);
            DataOutputStream toWrite = new DataOutputStream(clientSocket.getOutputStream());
            if (fileName != "") {
                writeFile(fileName, toWrite);
            } else
                System.out.println(toWrite);
            // Cleanup
            clientSocket.close();
            toWrite.close();

        } catch (UnknownHostException uh) {
            System.out.println(uh);
        } catch (IOException io) {
            System.out.println(io);
        }
    }
}
