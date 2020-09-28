import java.util.Scanner;

import implementation.EncryptFile;
import questions.Questions;
import utils.InputUtils;

public class App {
	private static final Questions QUESTIONS = new Questions();
	private static final EncryptFile IMPLEMENTATION = new EncryptFile();

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			boolean keepLoop = true;

			while (keepLoop) {
				System.out.println("Digite o que deseja fazer:");
				System.out.println("1 - Executar a implementação.\n2 - Executar as questões.\n0 - Sair");

				int option = InputUtils.verifyUserInput(input.nextLine());

				switch (option) {
				case 0:
					keepLoop = false;
					break;
				case 1:
					IMPLEMENTATION.main(input);
					break;

				case 2:
					QUESTIONS.main(input);
					break;

				default:
					System.out.println("Opção inexistente, tente novamente.");
					break;
				}
			}

			System.out.println("Adeus (:");
		} catch (Exception e) {
			e.printStackTrace();
		}
		input.close();
	}
}