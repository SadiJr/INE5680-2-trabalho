package questions;

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
	private Key key;
	private GCMParameterSpec gcm;
	private Cipher c;

	private String encrypt(String password, String message) throws Exception {
		String salt = generateSalt();

		c = Cipher.getInstance("AES/GCM/NoPadding");

		key = generateKey(password, salt);
		gcm = new GCMParameterSpec(128, salt.getBytes());

		c.init(Cipher.ENCRYPT_MODE, key, gcm);
		return Hex.encodeHexString(c.doFinal(message.getBytes()));
	}

	private Key generateKey(String password, String salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		SecretKey derivedKey = factory
				.generateSecret(new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10000, 256));
		return new SecretKeySpec(derivedKey.getEncoded(), "AES");
	}

	private String decrypt(String message) throws Exception {
		c.init(Cipher.DECRYPT_MODE, key, gcm);
		byte[] decodeHex = c.update(Hex.decodeHex(message));
		return new String(c.doFinal(decodeHex));
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

			String encrypt = encrypt(password, message);
			System.out.println("Mensagem criptografada: " + encrypt);

			String decrypt = decrypt(encrypt);
			System.out.println("Mensagem decriptografada: " + decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}