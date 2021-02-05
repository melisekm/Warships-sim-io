package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOHandler {
    Scanner sc;

    public String readBoard(String location) {
        StringBuilder res = new StringBuilder();
        File boardTxt = new File(location);
        try {
            this.sc = new Scanner(boardTxt);

            while (sc.hasNextLine()) {
                res.append(sc.nextLine()).append("\n");
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Subor nenajdeny.");
            e.printStackTrace();
        }
        return res.toString();
    }

    public static int checkArgs(String[] args) {
        int signal = 0;
        if (args.length == 0) {
            signal = 1;
        } else {
            if (!(args[0].equals("online") || args[1].equals("local"))) {
                signal = 2;
            }
            if (args[1].equals("server") && args.length == 2) {
                signal = 3;
            } else if (!(args[1].equals("server") || args[1].equals("client"))) {
                signal = 4;
            }
        }
        if (signal > 0) {
            printHelp();
        }
        return signal;
    }

    public static void printHelp() {
        System.out.println("Usage: 'online'/'local' 'client'/'server' <port>");
    }
}
