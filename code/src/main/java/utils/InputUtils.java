package utils;

public class InputUtils {
	public static int verifyUserInput(String question) {
		try {
			return Integer.valueOf(question);
		} catch (Exception e) {
			System.out.println("Digite apenas números, imbecil!");
		}
		return -1;
	}
}