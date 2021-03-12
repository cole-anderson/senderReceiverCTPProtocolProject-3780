
//Client Side
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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

        // TODO: SOCKET STUFF
    }

    /*
     * readFile: reads a textfile into an object
     */
    static void readFile(String fileName) {
        // TODO: readfile
    }

    static String messageMode() {
        String input = "";
        System.out.println("Enter your message");// DEL: DELETE THIS LINE
        Scanner inline = new Scanner(System.in);
        input = inline.nextLine();
        inline.close();
        return input;

    }

    public static void clientSide(String address, int port, String fileName, String message) {
        try {
            Socket clientSocket = new Socket(address, port);

            OutputStream output = clientSocket.getOutputStream();
            DataOutputStream transmission = new DataOutputStream(output);

            transmission.writeUTF(message);
            transmission.flush();
            transmission.close();
            // Cleanup
            clientSocket.close();

        } catch (UnknownHostException uh) {
            System.out.println(uh);
        } catch (IOException io) {
            System.out.println(io);
        }
    }
}
