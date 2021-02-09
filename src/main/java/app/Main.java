package app;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        int signal = app.parseArgs(args);
        if (signal > 0) {
            app.printHelp();
            System.exit(signal);//TODO signals can be made to constants.
        }
        app.run();
    }

}
