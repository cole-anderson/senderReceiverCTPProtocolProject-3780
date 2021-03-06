public class Receiver {
    public static void main(String[] args) {
        System.out.println("args length test: " + args.length);
        /*
         * possible command line arguments(args.length == 3 or 1) Receiver -f text.txt
         * port Receiver port
         */
        // If -f command line argument present and host and port present
        if (args[1] == "-f" && args.length == 3) {
            System.out.println("Data file selected for transmission: " + args[2]);

        } else {
            System.out.println("Message mode, no file specified");
        }
    }

    /*
     * readFile: reads a textfile into an object
     */
    void readFile(String fileName) {
        //
    }
}
