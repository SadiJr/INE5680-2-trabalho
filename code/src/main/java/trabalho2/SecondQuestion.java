package trabalho2;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class SecondQuestion {
	private static final SecondQuestion obj = new SecondQuestion();
	private Key key;
	private Cipher cipher;
	private IvParameterSpec iv;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			System.out.println("Digite uma frase qualquer: ");
			String message = input.nextLine();

			System.out.printf("%nA mensagem cifrado é: %s%n", obj.encrypt(message));
		} catch (Exception e) {
			System.err.printf("%nUm erro ocorreu durante a execução: %s", e.getMessage());
		} finally {
			input.close();
		}

	}

	private void init() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
		cipher = Cipher.getInstance("AES/CTR/NoPadding");
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256);
		Key transactionKey = generator.generateKey();
		key = new SecretKeySpec(transactionKey.getEncoded(), "AES");

		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		byte[] transactionIv = new byte[16];
		random.nextBytes(transactionIv);
		iv = new IvParameterSpec(transactionIv);
	}

	private String encrypt(String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		init();

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		return Hex.encodeHexString(cipher.doFinal(message.getBytes()));
	}

	private String decrypt(String message) throws InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, IllegalBlockSizeException,
			BadPaddingException, DecoderException {
		init();

		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(cipher.doFinal(Hex.decodeHex(message.toCharArray())));
	}
}