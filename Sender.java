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
        // If -f command line argument present and host and port present
        else if (args[1].equals("-f") && args.length == 3) {
            System.out.println("Data file selected for transmission: " + args[2]);
        }
        // If only host and port is present
        else if (args.length == 2) {
            System.out.println("Message mode, no file specified");
            // String message = messageMode();
            // args[2] = IP ADDRESS
            // args[3] = PORT

        }
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
}
