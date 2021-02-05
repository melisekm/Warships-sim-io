package app;

import java.util.Arrays;
import java.util.Scanner;

public class StdInputReader {
	private static final Scanner sc = new Scanner(System.in);
	public static String loopedInput(String prompt, String... accepted) {
		while (true) {
			System.out.println(prompt);
			String option = sc.nextLine();
			if (Arrays.asList(accepted).contains(option)) {
				return option;
			}
		}
	}

	public static String getInput(String prompt) {
		System.out.println(prompt);
		return sc.nextLine();
	}
}
