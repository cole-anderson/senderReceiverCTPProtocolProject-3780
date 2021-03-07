public class Receiver {
    public static void main(String[] args) {
        // TEST
        for (int i = 0; i < args.length; i++) {
            System.out.println("args at" + i + ":" + args[i]);
        }
        // Convert args to string which we then parse
        for (int i = 0; i < args.length; i++) {

        }

        //
        /*
         * Receiver -f data-received.txt 64341 [OR] Receiver 64341
         */

        // If command line argument does not contain a port
        // if (args.length == 0 || args.length == 2) {
        // System.out.println("No Port Specified or incorrect commandline input, Exiting
        // Program");
        // System.exit(0);
        // } else if (args.length == 1) {
        // System.out.println("No data file selected for receiving data, outputting to
        // terminal");

        // }
        // // If -f command line argument present and port is present
        // else if (args[0] == "-f") {
        // System.out.println("Data file selected for receiving data: " + args[1]);

        // }
        // TODO: SOCKET STUFF
    }

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName) {
        // TODO: writefile
    }

}
