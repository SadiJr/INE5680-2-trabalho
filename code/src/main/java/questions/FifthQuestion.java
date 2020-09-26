package questions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class FifthQuestion {
	private static final FifthQuestion obj = new FifthQuestion();

	private String encrypt(String password, String message) throws Exception {
		String salt = generateSalt();

		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		c.init(Cipher.ENCRYPT_MODE, generateKey(password, salt), new GCMParameterSpec(128, salt.getBytes()));

		return new String(Hex.encodeHex(c.doFinal(message.getBytes())));
	}

	private Key generateKey(String password, String salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		SecretKey derivedKey = factory
				.generateSecret(new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10000, 256));
		return new SecretKeySpec(derivedKey.getEncoded(), "AES");
	}

	private String decrypt(String password, String message)
			throws InvalidKeyException, InvalidAlgorithmParameterException, Exception {
		String salt = generateSalt();

		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		c.init(Cipher.DECRYPT_MODE, generateKey(password, salt), new GCMParameterSpec(128, salt.getBytes()));
		c.update(Hex.decodeHex(message));
		return new String(Hex.encodeHex(c.doFinal(Hex.decodeHex(message))));
	}

	private String generateSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstanceStrong();
		byte[] salt = new byte[96];
		sr.nextBytes(salt);
		return Hex.encodeHexString(salt);
	}

	public void main(Scanner input) {
		try {
			System.out.println("Digite algo:");
			String message = input.nextLine();

			System.out.println("Digite a senha:");
			String password = input.nextLine();

			String encrypt = obj.encrypt(password, message);
			System.out.println("Mensagem criptografada: " + encrypt);

			String decrypt = obj.decrypt(password, encrypt);
			System.out.println("Mensagem decriptografada: " + decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}