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
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secretKey);

		return new String(Hex.encode(sha256_HMAC.doFinal(message.getBytes())));
	}

	private String calcHash(String message) throws NoSuchAlgorithmException {
		Security.addProvider(new BouncyCastleProvider());

		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
		return new String(Hex.encode(messageDigest.digest(message.getBytes())));
	}

	public void main(Scanner input) {
		try {
			System.out.println("Digite a frase:");
			String message = input.nextLine();
			
			System.out.println("O hash é = " + calcHash(message));
			
			System.out.println("Digite a chave:");
			String key = input.nextLine();

			System.out.println("O MAC é = " + obj.calcMAC(message, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
