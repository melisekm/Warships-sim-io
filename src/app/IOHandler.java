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
}
