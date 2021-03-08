import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {

    public static void main(String[] args) {
        /*
         * sender -f data.txt 127.0.0.1 64341 [-f, file, ipaddress, port] ........
         * sender 127.0.0.1 64341 [ipaddress,port]
         */

        // If command line argument does not contain a host and port(exit)
        if (args.length <= 1) {
            System.out.println("No Port Specified, Exiting Program");
            System.exit(0);
        }
        // Sender -f data.txt 127.0.0.1 63431
        else if (args[0].equals("-f") && args.length == 4) {
            System.out.println("Data file selected for transmission: " + args[1]);
        }
        // Sender 127.0.0.1 64341
        else if (args.length == 2) {
            System.out.println("Message mode, no file specified");
            String message = messageMode();

        }
        // Initialize values
        int port = Integer.parseInt(args[args.length - 1]);
        String address = args[args.length - 2];
        System.out.println("fuck");
        connect(address, port);

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
        System.out.println("Enter your message");
        // TODO:

        return input;

    }

    public static void connect(String address, int port) {
        System.out.println("Attempting Connection on port " + port + " at address " + address);
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Socket Connect on port" + port);
        } catch (UnknownHostException un) {
            System.out.println(un);

        } catch (IOException io) {
            System.out.println(io);
        }

    }
}
