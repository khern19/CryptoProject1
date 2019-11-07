package project1;

import project1.SDES;

public class TripleDES {

	public static void main(String[] args) {
		System.out.println("-----------------------------------------------------------------");
		System.out.println("|  Raw Key 1   |   Raw Key 2   |   Plain Text   |  Cipher Text  |");
		System.out.println("-----------------------------------------------------------------");
		encrypt();
		decreypt();
		System.out.println("-----------------------------------------------------------------");
	}

	private static void encrypt() {
		byte[][] keys1 = new byte[4][10];
		byte[][] keys2 = new byte[4][10];
		byte[][] plaintext = new byte[4][8];
		byte[][] ciphertext = new byte[4][8];

		// hard code table for key 1
		keys1[0] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		keys1[1] = new byte[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		keys1[2] = new byte[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		keys1[3] = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		// hard code table for key 2
		keys2[0] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		keys2[1] = new byte[] { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		keys2[2] = new byte[] { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		keys2[3] = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		// hard code table for plain text
		plaintext[0] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[] { 1, 1, 0, 1, 0, 1, 1, 1 };
		plaintext[2] = new byte[] { 1, 0, 1, 0, 1, 0, 1, 0 };
		plaintext[3] = new byte[] { 1, 0, 1, 0, 1, 0, 1, 0 };

		// takes in one key 1 and key 2 and plain text at a time and prints the results
		for (int i = 0; i < keys1.length; i++) {
			ciphertext[i] = Encrypt(keys1[i], keys2[i], plaintext[i]);
			System.out.printf("|%12s  |%12s   |%12s    |%11s    |\n", printArray(keys1[i]), printArray(keys2[i]),
					printArray(plaintext[i]), printArray(ciphertext[i]));
		}
	}

	private static byte[] Encrypt(byte[] k1, byte[] k2, byte[] plaintext) {
		// E3DES(p) = EDES(k1,DDES(k2,EDES(k1, p)))
		return SDES.Encrypt(k1, SDES.Decrypt(k2, SDES.Encrypt(k1, plaintext)));
	}

	private static void decreypt() {
		byte[][] key1 = new byte[4][];
		byte[][] key2 = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		// hard code table for key 1
		key1[0] = new byte[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key1[1] = new byte[] { 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 };
		key1[2] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key1[3] = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		// hard code table for key 2
		key2[0] = new byte[] { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[1] = new byte[] { 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[2] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key2[3] = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		// hard code table for cipher text
		ciphertext[0] = new byte[] { 1, 1, 1, 0, 0, 1, 1, 0 };
		ciphertext[1] = new byte[] { 0, 1, 0, 1, 0, 0, 0, 0 };
		ciphertext[2] = new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 };
		ciphertext[3] = new byte[] { 1, 0, 0, 1, 0, 0, 1, 0 };

		// takes in one key 1 and key 2 and cipher text at a time and prints the results
		for (int i = 0; i < 4; i++) {
			plaintext[i] = Decrypt(key1[i], key2[i], ciphertext[i]);
			System.out.printf("|%12s  |%12s   |%12s    |%11s    |\n", printArray(key1[i]), printArray(key2[i]),
					printArray(plaintext[i]), printArray(ciphertext[i]));
		}
	}

	static byte[] Decrypt(byte[] k1, byte[] k2, byte[] ciphertext) {
		// D3DES(c) = DDES(k1,EDES(k2,DDES(k1, c)))
		return SDES.Decrypt(k1, SDES.Encrypt(k2, SDES.Decrypt(k1, ciphertext)));
	}

	private static String printArray(byte[] array) {
		// basic print array method
		String result = "";
		for (int i = 0; i < array.length; i++)
			result += array[i];
		return result;
	}
}
