package app;

import java.util.Arrays;
import java.util.Scanner;

public class InputReader {
	Scanner sc = new Scanner(System.in);

	public String login() {
		String option;
		while (true) {
			System.out.println("Server[s]/Client[c]/Quit[q]");
			option = sc.nextLine();
			if (Arrays.asList(new String[] { "s", "c", "q" }).contains(option)) {
				break;
			}
		}
		return option;
	}
}
