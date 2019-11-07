package project1;

import java.util.Arrays;

public class SDES {
	private static byte[][] s0Table = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 3, 2 } };

	private static byte[][] s1Table = { { 0, 1, 2, 3 }, { 2, 0, 1, 3 }, { 3, 0, 1, 0 }, { 2, 1, 0, 3 } };

	public static void main(String[] args) {

		System.out.println("-----------------------------------------------");
		System.out.println("|  Raw Key   |   Plain Text   |  Cipher Text  |");
		System.out.println("-----------------------------------------------");
		encrypt();
		decreypt();
		System.out.println("-----------------------------------------------");
	}

	private static void encrypt() {
		byte[][] keys = new byte[4][10];
		byte[][] plaintext = new byte[4][8];
		byte[][] ciphertext = new byte[4][8];

		// hard code table keys
		keys[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		keys[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		keys[2] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		keys[3] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };

		// hard code table for plain text
		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };
		plaintext[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };

		// takes in one key and plain text at a time and prints the results
		for (int i = 0; i < keys.length; i++) {
			ciphertext[i] = Encrypt(keys[i], plaintext[i]);
			System.out.printf("|%11s |%12s    |%11s    |\n", printArray(keys[i]), printArray(plaintext[i]),
					printArray(ciphertext[i]));
		}
	}

	public static byte[] Encrypt(byte[] key, byte[] plain) {
		// key 1 and key 2
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];

		// getting the key from (key,k1,k2)
		createKeys(key, k1, k2);

		// create the final cipher text
		byte[] cipher = new byte[plain.length];

		// encrypting the block
		for (int i = 0; i < plain.length; i += 8) {
			byte[] subplaintext = Arrays.copyOfRange(plain, i, i + 8);
			byte[] temp = encryptBlock(k1, k2, subplaintext);
			for (int j = 0; j < 8; j++) {
				cipher[j + i] = temp[j];
			}
		}
		return cipher;
	}

	private static byte[] encryptBlock(byte[] k1, byte[] k2, byte[] plain) {
		// initial permutation from book
		// 2 6 3 1 4 8 5 7
		// to index
		// 1 5 2 0 3 7 4 6
		byte[] init = initialPermute(plain);

		// XOR for (initial,k1)
		fk(init, k1);

		// swap using
		// 4 5 6 7 0 1 2 3
		init = swap(init);

		// XOR for (initial,k2)
		fk(init, k2);

		// final permutation from book
		init = finalPermute(init);

		return init;
	}

	private static void decreypt() {
		byte[][] keys = new byte[4][10];
		byte[][] plaintext = new byte[4][8];
		byte[][] ciphertext = new byte[4][8];

		// hard code table keys
		keys[0] = new byte[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		keys[1] = new byte[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		keys[2] = new byte[] { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };
		keys[3] = new byte[] { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };

		// hard code table for cipher text
		ciphertext[0] = new byte[] { 0, 0, 0, 1, 1, 1, 0, 0 };
		ciphertext[1] = new byte[] { 1, 1, 0, 0, 0, 0, 1, 0 };
		ciphertext[2] = new byte[] { 1, 0, 0, 1, 1, 1, 0, 1 };
		ciphertext[3] = new byte[] { 1, 0, 0, 1, 0, 0, 0, 0 };

		// takes in one key and cipher text at a time and prints the results
		for (int i = 0; i < keys.length; i++) {
			plaintext[i] = Decrypt(keys[i], ciphertext[i]);
			System.out.printf("|%11s |%12s    |%11s    |\n", printArray(keys[i]), printArray(plaintext[i]),
					printArray(ciphertext[i]));
		}
	}

	static byte[] Decrypt(byte[] key, byte[] cipher) {
		// key 1 and key 2
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];

		// getting the key from (key,k1,k2)
		createKeys(key, k1, k2);

		// create the final plain text
		byte[] plain = new byte[cipher.length];

		// decrypting the block
		for (int i = 0; i < cipher.length; i += 8) {
			byte[] subplaintext = Arrays.copyOfRange(cipher, i, i + 8);
			byte[] temp = decryptBlock(k1, k2, subplaintext);
			for (int j = 0; j < 8; j++) {
				plain[j + i] = temp[j];
			}
		}
		return plain;
	}

	private static byte[] decryptBlock(byte[] k1, byte[] k2, byte[] cipher) {
		// initial permutation from book
		// 2 6 3 1 4 8 5 7
		// to index
		// 1 5 2 0 3 7 4 6
		byte[] init = initialPermute(cipher);

		// XOR for (initial,k2)
		fk(init, k2);

		// swap using
		// 4 5 6 7 0 1 2 3
		init = swap(init);

		// XOR for (initial,k1)
		fk(init, k1);

		// final permutation from book
		init = finalPermute(init);
		return init;
	}

	private static byte[] initialPermute(byte[] plain) {
		// using books indexes
		byte[] result = { plain[1], plain[5], plain[2], plain[0], plain[3], plain[7], plain[4], plain[6] };
		return result;
	}

	private static void fk(byte[] init, byte[] k1) {
		// using s0 and s1 from book
		byte[] temp = fkHelper(init, k1);
		for (int i = 0; i < 4; i++)
			init[i] = (byte) (init[i] ^ temp[i]);
	}

	private static byte[] fkHelper(byte[] init, byte[] k1) {
		// using books indexes
		byte[] temp = { init[7], init[4], init[5], init[6], init[5], init[6], init[7], init[4] };

		// XOR for the temp and key1
		for (int i = 0; i < 8; i++)
			temp[i] = (byte) (temp[i] ^ k1[i]);

		// get new row and col to work with
		byte row = (byte) (temp[0] * 2 + temp[3]);
		byte col = (byte) (temp[1] * 2 + temp[2]);

		// s0 is used from the table provided
		byte value = s0Table[row][col];

		// updating values from current row and col
		temp[0] = (byte) (value / 2);
		temp[1] = (byte) (value % 2);

		// get new row and col to work with
		row = (byte) (temp[4] * 2 + temp[7]);
		col = (byte) (temp[5] * 2 + temp[6]);

		// s1 is used from the table provided
		value = s1Table[row][col];

		// updating values from current row and col
		temp[2] = (byte) (value / 2);
		temp[3] = (byte) (value % 2);

		// result using
		// from book
		// 2 4 3 1
		// index
		// 1 3 2 0
		byte[] output = { temp[1], temp[3], temp[2], temp[0] };

		return output;
	}

	private static byte[] swap(byte[] init) {
		// using books indexes
		byte[] output = { init[4], init[5], init[6], init[7], init[0], init[1], init[2], init[3] };
		return output;
	}

	private static byte[] finalPermute(byte[] init) {
		// using books indexes
		byte[] output = { init[3], init[0], init[2], init[4], init[6], init[1], init[7], init[5] };
		return output;
	}

	private static void createKeys(byte[] key, byte[] k1, byte[] k2) {
		// from book
		// 3 5 2 7 4 10 1 9 8 6
		// to
		// 2 4 1 6 3 9 0 8 7 5
		byte[] p10 = createPTen(key);

		// shift to the left by 1
		byte[] s1 = shift(p10, 1);

		// turning 10 bit key to 8 bit key (s1,k1)
		// from book
		// 6 3 7 4 8 5 10 9
		// to
		// 5 2 6 3 7 4 9 8
		turn10to8(s1, k1);

		// shifting to the left by 2
		byte[] s2 = shift(s1, 2);

		// turning 10 bit key to 8 bit key (s2,k2)
		turn10to8(s2, k2);
	}

	private static void turn10to8(byte[] s, byte[] k) {
		//ignoring indexes 0 and 1
		k[0] = s[5];
		k[1] = s[2];
		k[2] = s[6];
		k[3] = s[3];
		k[4] = s[7];
		k[5] = s[4];
		k[6] = s[9];
		k[7] = s[8];
	}

	private static byte[] shift(byte[] p10, int shift) {
		// shifting the first 5 bits
		byte[] output = new byte[p10.length];
		for (int i = 0; i < output.length; i++) {
			if (i >= 5) {
				output[i] = p10[(i + shift) % 5 + 5];
			} else {
				output[i] = p10[(i + shift) % 5];
			}
		}
		return output;
	}

	private static byte[] createPTen(byte[] key) {
		// using books indexes
		byte[] result = { key[2], key[4], key[1], key[6], key[3], key[9], key[0], key[8], key[7], key[5] };
		return result;
	}

	private static String printArray(byte[] array) {
		//basic print array method
		String result = "";
		for (int i = 0; i < array.length; i++)
			result += array[i];
		return result;
	}

}
