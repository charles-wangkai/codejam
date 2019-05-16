import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			BigInteger N = sc.nextBigInteger();
			int L = sc.nextInt();
			BigInteger[] ciphertext = new BigInteger[L];
			for (int i = 0; i < ciphertext.length; i++) {
				ciphertext[i] = sc.nextBigInteger();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(N, ciphertext)));
		}

		sc.close();
	}

	static String solve(BigInteger N, BigInteger[] ciphertext) {
		BigInteger[] primes = new BigInteger[ciphertext.length + 1];

		int index = 0;
		while (ciphertext[index].equals(ciphertext[index + 1])) {
			index++;
		}

		primes[index + 1] = ciphertext[index].gcd(ciphertext[index + 1]);
		for (int i = index; i >= 0; i--) {
			primes[i] = ciphertext[i].divide(primes[i + 1]);
		}
		for (int i = index + 2; i < primes.length; i++) {
			primes[i] = ciphertext[i - 1].divide(primes[i - 1]);
		}

		BigInteger[] sortedDistinctPrimes = Arrays.stream(primes).distinct().sorted().toArray(BigInteger[]::new);
		Map<BigInteger, Character> primeToLetter = new HashMap<>();
		for (int i = 0; i < sortedDistinctPrimes.length; i++) {
			primeToLetter.put(sortedDistinctPrimes[i], (char) ('A' + i));
		}

		return Arrays.stream(primes).map(primeToLetter::get)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}
}
