package questions;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class FourthQuestion {
	private static final FourthQuestion obj = new FourthQuestion();

	private String calcMAC(String message, String key) throws InvalidKeyException, NoSuchAlgorithmException {
		Mac mac = Mac.getInstance("HmacSHA512");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
		mac.init(secretKey);

		return new String(Hex.encode(mac.doFinal(message.getBytes())));
	}

	private String calcHash(String message) throws NoSuchAlgorithmException {
		Security.addProvider(new BouncyCastleProvider());

		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
		return new String(Hex.encode(messageDigest.digest(message.getBytes())));
	}

	public void main(Scanner input) {
		try {
			System.out.println("Digite a frase 1:");
			String message = input.nextLine();
			
			System.out.println("Digite a chave:");
			String key = input.nextLine();

			System.out.println("O hash é = " + calcHash(message));
			System.out.println("O MAC é = " + obj.calcMAC(message, key));
			
			System.out.println("Digite a frase 2:");
			String message2 = input.nextLine();
			
			System.out.println("O hash é = " + calcHash(message2));
			System.out.println("O MAC é = " + obj.calcMAC(message2, key));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
