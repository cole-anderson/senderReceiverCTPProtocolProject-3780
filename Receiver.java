public class Receiver {
    public static void main(String[] args) {
        System.out.println("args length test: " + args.length);
        /*
         * possible command line arguments(args.length == 3 or 1) Receiver -f text.txt
         * port Receiver port
         */

        // If command line argument does not contain a port
        if (args.length < 2) {
            System.out.println("No Port Specified, Exiting Program");
            System.exit(0);
        }
        // If -f command line argument present and host and port present
        else if (args[1] == "-f" && args.length == 3) {
            System.out.println("Data file selected for transmission: " + args[2]);

        } else if (args.length == 2) {
            System.out.println("Message mode, no file specified");
            String message = messageMode(args[2]);

        }

        // SOCKET STUFF
    }

    /*
     * readFile: reads a textfile into an object
     */
    static void readFile(String fileName) {
        //
    }

    static String messageMode(String port) {
        String input = "";
        System.out.println("Enter your message");
        // TODO: add read here

        return input;

    }
}
