package app;

import java.util.Arrays;
import java.util.Scanner;

public class InputReader {
	static Scanner sc = new Scanner(System.in);

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

	public static String getInput(String prompt) {
		System.out.println(prompt);
		String sprava = sc.nextLine();
		return sprava;
	}
}
