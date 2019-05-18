import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int K = sc.nextInt();
			int[] C = readArray(sc, N);
			int[] D = readArray(sc, N);

			System.out.println(String.format("Case #%d: %d", tc, solve(C, D, K)));
		}

		sc.close();
	}

	static int[] readArray(Scanner sc, int size) {
		int[] result = new int[size];
		for (int i = 0; i < result.length; i++) {
			result[i] = sc.nextInt();
		}
		return result;
	}

	static int solve(int[] C, int[] D, int K) {
		int result = 0;
		for (int L = 0; L < C.length; L++) {
			for (int R = L; R < C.length; R++) {
				if (Math.abs(findMax(C, L, R) - findMax(D, L, R)) <= K) {
					result++;
				}
			}
		}
		return result;
	}

	static int findMax(int[] values, int L, int R) {
		return IntStream.rangeClosed(L, R).map(i -> values[i]).max().getAsInt();
	}
}
