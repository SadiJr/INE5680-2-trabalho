package trabalho2;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ThirdQuestion {
	private static final ThirdQuestion obj = new ThirdQuestion();

	private Key key;
	private Cipher cipher;
	private IvParameterSpec iv;

	private String aesCBCDecrypt(String keyUser, String ivUser, String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DecoderException {
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		iv = new IvParameterSpec(Hex.decodeHex(ivUser.toCharArray()));
		key = new SecretKeySpec(Hex.decodeHex(keyUser.toCharArray()), "AES");

		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(cipher.doFinal(Hex.decodeHex(message.toCharArray())));

	}

	private String aesCTRDecrypt(String keyUser, String ivUser, String message)
			throws DecoderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		cipher = Cipher.getInstance("AES/CTR/NoPadding");
		iv = new IvParameterSpec(Hex.decodeHex(ivUser.toCharArray()));
		key = new SecretKeySpec(Hex.decodeHex(keyUser.toCharArray()), "AES");

		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(cipher.doFinal(Hex.decodeHex(message.toCharArray())));
	}

	public static void main(String[] args) {
		try {
			Scanner input = new Scanner(System.in);

			System.out.println("Digite a chave: ");
			String keyUser = input.nextLine();

			System.out.println("Digite o IV:");
			String ivUser = input.nextLine();

			System.out.println("Digite o texto cifrado");
			String message = input.nextLine();

//			String plainText = obj.aesCBCDecrypt(keyUser, ivUser, message);

			String plainText = obj.aesCTRDecrypt(keyUser, ivUser, message);
			System.out.println("Texto decifrado = " + plainText);
		} catch (Exception e) {
			System.err.println("Algo de errado não está certo!" + e.getMessage());
			e.printStackTrace();
		}
	}

}