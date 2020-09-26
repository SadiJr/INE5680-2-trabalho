package questions;

import java.util.Scanner;

public class Main {
	private static final SecondQuestion second = new SecondQuestion();
	private static final ThirdQuestion third = new ThirdQuestion();
	private static final FourthQuestion fourth = new FourthQuestion();
	private static final FifthQuestion fifth = new FifthQuestion();

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		boolean keepLoop = true;

		try {
			while (keepLoop) {
				
				System.out.println("Qual questão deseja executar? 2, 3, 4 ou 5?\n0 - sair");

				String question = input.nextLine();
				
				int option = InputUtils.verifyUserInput(question);
				

				switch (option) {
				case 0:
					keepLoop = false;
					break;
				case 2:
					second.main(input);
					break;

				case 3:
					third.main(input);
					break;
					
				case 4:
					fourth.main(input);
					break;
					
				case 5:
					fifth.main(input);
					break;

				default:
					System.out.println("Opção inexistente, tente novamente.");
					break;
				}
			}
			
			System.out.println("Adeus (:");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
}
