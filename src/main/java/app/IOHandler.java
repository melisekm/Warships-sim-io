package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOHandler {
    Scanner sc;

    public String readBoard(String location) throws FileNotFoundException {
        StringBuilder res = new StringBuilder();
        File boardTxt = new File(location);
        this.sc = new Scanner(boardTxt);

        while (sc.hasNextLine()) {
            res.append(sc.nextLine()).append("\n");
        }
        sc.close();

        return res.toString();
    }
}
