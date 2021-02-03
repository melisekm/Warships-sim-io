package app;

import java.util.Arrays;
import java.util.Scanner;

public class InputReader {
	static Scanner sc = new Scanner(System.in);

	public String login() {
		while (true) {
			System.out.println("Server[s]/Client[c]/Quit[q]");
			String option = sc.nextLine();
			if (Arrays.asList(new String[] { "s", "c", "q" }).contains(option)) {
				return option;
			}
		}
	}

	public static String getInput(String prompt) {
		System.out.println(prompt);
		return sc.nextLine();
	}
}
