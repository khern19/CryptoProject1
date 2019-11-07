package project1;

public class DecodeMessage {

	public static void main(String[] args) {
		
		part1();
		part2();
		/*
		 * key: 1011110100
		 * 
		 * message: WHOEVER THINKS HIS PROBLEM CAN BE SOLVED USING CRYPTOGRAPHY, DOESN'T
		 * UNDERSTAND HIS PROBLEM AND DOESN'T UNDERSTAND CRYPTOGRAPHY. ATTRIBUTED BY
		 * ROGER NEEDHAM AND BUTLER LAMPSON TO EACH OTHER
		 */
		part3();
		/*
		 * 1st key: 1110000101 2nd key: 0101100011
		 * 
		 * Message: THERE ARE NO SECRETS BETTER KEPT THAN THE SECRETS THAT EVERYBODY
		 * GUESSES.
		 */
	}
	private static void part1() {
		byte[] OGKey = { 0, 1, 1, 1, 0, 0, 1, 1, 0, 1 };
		String message = "CRYPTOGRAPHY";
		//get plain text from original key
		byte[] plaintext = CASCII.Convert(message);
		
		//get cipher text from original key
		byte[] ciphertext = SDES.Encrypt(OGKey, plaintext);
		
		System.out.println("------------------------------Part 1------------------------------");

		//prints out original message
		System.out.println("Message: \n"+ message);
		System.out.println("------------------------------------------------------------------");
		
		//prints out pain text
		System.out.println("Plaintext (CASCII): \n"+ printArray(plaintext));
		System.out.println("------------------------------------------------------------------");
		
		//prints out cipher text (CASCII)
		System.out.println("Encryted message: \n"+CASCII.toString(ciphertext));
		System.out.println("------------------------------------------------------------------");

		byte[] decrypt = SDES.Decrypt(OGKey, ciphertext);

		//prints out decrypt message
		System.out.println("Decrypted text: \n"+printArray(decrypt));
		System.out.println("------------------------------------------------------------------");

		//print dectypt message (CASCII)
		System.out.println("Decrypted message: \n"+CASCII.toString(decrypt));
	}

	private static void part2() {
		String encrypted = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
		byte[] key = new byte[10];
		byte[] plaintext;
		byte[] ciphertext = getBytes(encrypted);

		// gets the number of possible keys to try
		int size = (int) Math.pow(2, key.length);
		String currentString = "";
		String resultKey = "";
		String resultMessage = "";

		// tries different keys and checks if the current string makes a real sentence
		for (int i = 0; i < size; i++) {
			plaintext = SDES.Decrypt(key, ciphertext);
			currentString = CASCII.toString(plaintext);

			// checks if "the" is in the current string and check punctuation
			if (currentString.contains("THE") && punctuation(currentString)) {
				resultKey = printArray(key);
				resultMessage = currentString;
				break;
			}
			// increments the key by 1
			keyPlusPlus(key);
		}
		System.out.println("-----------------------Part 2-----------------------");
		System.out.println("key: \n" + resultKey);
		System.out.println("----------------------------------------------------");
		System.out.println("Message: \n" + resultMessage);
	}

	private static String printArray(byte[] array) {
		// basic print array method
		String result = "";
		for (int i = 0; i < array.length; i++)
			result += array[i];
		return result;
	}

	private static void keyPlusPlus(byte[] key) {
		// changes the bytes of key to increment it
		for (int i = 0; i < key.length; i++) {
			if (key[i] == 0) {
				key[i] = 1;
				return;
			} else {
				key[i] = 0;
			}
		}
	}

	private static boolean punctuation(String currentPlainText) {
		// checks if the punctuation is correct
		for (int i = 0; i < currentPlainText.length() - 1; i++) {
			char currentStr = currentPlainText.charAt(i);
			if ((currentStr == '.' || currentStr == '?' || currentStr == ',' || currentStr == ':')
					&& currentPlainText.charAt(i + 1) != ' ')
				return false;
		}
		return true;

	}

	private static byte[] getBytes(String str) {
		// turns the bytes from string
		byte[] result = new byte[str.length()];

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '1') {
				result[i] = (byte) 1;
			} else {
				result[i] = (byte) 0;
			}
		}
		return result;
	}

	private static void part3() {
		String encrypted = "00011111100111111110011111101100111000000011001011110010101010110001011101001101000000110011010111111110000000001010111111000001010010111001111001010101100000110111100011111101011100100100010101000011001100101000000101111011000010011010111100010001001000100001111100100000001000000001101101000000001010111010000001000010011100101111001101111011001001010001100010100000";

		byte[] k1 = new byte[10];
		byte[] k2 = new byte[10];
		byte[] plaintext;
		byte[] ciphertext = getBytes(encrypted);
		
		// gets the number of possible keys to try
		int size1 = (int) Math.pow(2, k1.length);
		int size2 = (int) Math.pow(2, k2.length);
		String currentString = "";
		String resultKey1 = "";
		String resultKey2 = "";
		String resultMessage = "";
		
		// tries different keys and checks if the current string makes a real sentence
		for (int i = 0; i < size1; i++) {
			for (int j = 0; j < size2; j++) {
				plaintext = TripleDES.Decrypt(k1, k2, ciphertext);
				currentString = CASCII.toString(plaintext);

				// checks if "the" is in the current string and check punctuation
				if (currentString.contains("THE") && punctuation(currentString)) {
					resultKey1 = printArray(k1);
					resultKey2 = printArray(k2);
					resultMessage = currentString;
					break;
				}
				// increments the key by 1
				keyPlusPlus(k1);
			}
			// increments the key by 1
			keyPlusPlus(k2);
		}
		System.out.println("-----------------------Part 3-----------------------");
		System.out.println("1st key: \n" + resultKey1);
		System.out.println("2nd key: \n" + resultKey2);
		System.out.println("----------------------------------------------------");
		System.out.println("Message: \n" + resultMessage);
	}
}
