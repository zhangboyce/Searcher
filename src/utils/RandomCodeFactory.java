package utils;

import java.util.Random;

/**
 * * 功能：随便数字字符串工厂
 * 
 */
public class RandomCodeFactory {

	private static char[] ca = new char[] { '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * 产生一个给定长度的数字字串
	 * 
	 * @param n
	 * @return
	 */
	public static String generate(int n) {
		Random random = new Random();
		char[] cr = new char[n];
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(10);
			cr[i] = Integer.toString(x).charAt(0);
		}
		return (new String(cr));
	}

	public static String generateMixed(int n) {
		Random random = new Random();
		char[] cr = new char[n];
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(ca.length);
			cr[i] = ca[x];
		}
		return (new String(cr));
	}

	public static void main(String[] args) {
		System.out.println(ca.length);
		for (int i = 0; i < 1; i++) {
			System.out.println(generateMixed(32));
		}
	}
}
