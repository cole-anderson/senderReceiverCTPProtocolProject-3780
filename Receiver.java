public class Receiver {
    public static void main(String[] args) {
        /*
         * Receiver -f data-received.txt 64341 [OR] Receiver 64341
         */

        // (1)(EXAMPLE INPUT) Receiver -f data_received.txt 6431
        if (args[0].equals("-f") && args.length == 2) {
            System.out.println("Data file selected for receiving data: " + args[1]);
        }
        // (2)(EXAMPLE INPUT) Receiver 6431
        else if (args.length == 1) {
            System.out.println("No data file selected for receiving data. Outputting to terminal");
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
        // TODO: SOCKET STUFF
    }// ENDMAIN

    /*
     * writeFile: writes to textfile
     */
    static void writeFile(String fileName) {
        // TODO: writefile
    }

}
