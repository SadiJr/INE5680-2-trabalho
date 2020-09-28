package implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import utils.InputUtils;

public class EncryptFile {
	private String path;
	private String passphrase;
	private byte[] salt;

	public void main(Scanner input) {
		try {
			boolean keepLoop = true;

			while (keepLoop) {

				System.out.println("Digite:\n1 - Exibir o manual da aplicação\n2 - Usar a aplicação\n0 - Voltar");

				int option = InputUtils.verifyUserInput(input.nextLine());

				switch (option) {
				case 0:
					keepLoop = false;
					break;
				case 1:
					man();
					break;

				case 2:
					starter(input);
					break;

				default:
					System.out.println("Opção inexistente, tente novamente.");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void starter(Scanner input) throws Exception {
		System.out.println("Deseja:\n1 - Cifrar um arquivo\n2 - Decifrar um arquivo");

		int option = InputUtils.verifyUserInput(input.nextLine());

		switch (option) {
		case 1:
			encryptFile(input);
			break;

		case 2:
			decryptFile(input);
			break;

		default:
			System.out.println("Opção inexistente, tente novamente.");
			break;
		}
	}

	private void decryptFile(Scanner input) throws Exception {
		if (findFile(input)) {
			File encryptedFile = new File(path);

			String filename = encryptedFile.getName();
			String decryptedFilename = filename.replace("_crypt", "_decrypt");
			File decryptedFile = new File(encryptedFile.getParent() + File.separator + decryptedFilename);

			decrypt(encryptedFile, decryptedFile);
			passphrase = "";

			System.out.println(
					"Deseja excluir o arquivo cifrado? [y/n]\nQualquer opção diferente de y será considerada não.");

			String deleteFile = input.nextLine();
			try {
				if (deleteFile.equals("y") || deleteFile.equals("Y")) {
					Files.deleteIfExists(encryptedFile.toPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void decrypt(File encryptedFile, File decryptedFile) throws Exception {
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

		FileInputStream input = new FileInputStream(encryptedFile);
		byte[] ciphertext = input.readAllBytes();

		byte[] iv = Arrays.copyOfRange(ciphertext, 0, 32);
		salt = Arrays.copyOfRange(ciphertext, 32, 128);
		byte[] ct = Arrays.copyOfRange(ciphertext, 128, ciphertext.length);

		Key key = generateKey(passphrase, salt);
		GCMParameterSpec gcm = new GCMParameterSpec(128, iv);

		c.init(Cipher.DECRYPT_MODE, key, gcm);
		byte[] outputBytes = c.doFinal(ct);

		FileOutputStream outputStream = new FileOutputStream(decryptedFile);
		outputStream.write(outputBytes);

		input.close();
		outputStream.close();
	}

	private boolean findFile(Scanner input) {
		System.out.println("Digite o caminho completo do arquivo:");
		path = input.nextLine();

		if (StringUtils.isEmpty(path) || Files.exists(Paths.get(path))) {
			System.out.println("Digite a senha:");
			passphrase = input.nextLine();

			if (StringUtils.isEmpty(passphrase)) {
				System.out.println("A senha não pode ser vazia. Abortando...");
				return false;
			}
			return true;
		}
		return false;
	}

	private void encryptFile(Scanner input) throws Exception {
		if (findFile(input)) {
			File plainFile = new File(path);
			String filename = plainFile.getName();
			int extension = filename.lastIndexOf('.');

			String encryptedFilename = filename.substring(0, extension) + "_crypt" + filename.substring(extension);
			File encryptedFile = new File(plainFile.getParent() + File.separator + encryptedFilename);

			encrypt(plainFile, encryptedFile);
			passphrase = "";

			System.out.println(
					"Deseja excluir o arquivo plano? [y/n]\nQualquer opção diferente de y será considerada não.");

			String deleteFile = input.nextLine();
			try {
				if (deleteFile.equals("y") || deleteFile.equals("Y")) {
					Files.deleteIfExists(plainFile.toPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void encrypt(File plainFile, File encryptedFile) throws Exception {
		Cipher cypher = getCypher(passphrase);

		FileInputStream input = new FileInputStream(plainFile);
		FileOutputStream output = new FileOutputStream(encryptedFile);

		GCMParameterSpec iv = cypher.getParameters().getParameterSpec(GCMParameterSpec.class);
		output.write(iv.getIV());
		output.write(salt);
		salt = null;

		byte[] encrypt = cypher.doFinal(input.readAllBytes());
		output.write(encrypt);
		output.flush();

		input.close();
		output.close();
	}

	private Cipher getCypher(String passphrase) throws Exception {
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

		salt = generateSalt();
		Key key = generateKey(passphrase, salt);
		GCMParameterSpec gcm = new GCMParameterSpec(128, generateIv());

		c.init(Cipher.ENCRYPT_MODE, key, gcm);
		return c;
	}

	private byte[] generateSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstanceStrong();
		byte[] s = new byte[96];
		sr.nextBytes(s);
		return s;
	}

	private byte[] generateIv() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstanceStrong();
		byte[] iv = new byte[32];
		sr.nextBytes(iv);
		return iv;
	}

	private Key generateKey(String password, byte[] salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		SecretKey derivedKey = factory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 10000, 256));
		return new SecretKeySpec(derivedKey.getEncoded(), "AES");
	}

	private void man() {
		System.out.println("Essa é uma aplicação que objetiva cifrar arquivos usando AES no modo GCM");
		System.out.println(" e com derivação de chave (PBKFS2). Para usar essa aplicação, basta indicar");
		System.out.println(" o caminho completa até o arquivo que se deseja cifrar e seguir o passo-a-passo.");
	}
}