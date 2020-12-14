import java.util.Collections;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int B = sc.nextInt();
			int C = sc.nextInt();
			int[] M = new int[C];
			int[] S = new int[C];
			int[] P = new int[C];
			for (int i = 0; i < C; i++) {
				M[i] = sc.nextInt();
				S[i] = sc.nextInt();
				P[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(R, B, M, S, P)));
		}

		sc.close();
	}

	static long solve(int R, int B, int[] M, int[] S, int[] P) {
		long lower = 0;
		long upper = 2_000_000_000_000_000_000L;
		long result = -1;
		while (lower <= upper) {
			long middle = (lower + upper) / 2;

			if (isPossible(R, B, M, S, P, middle)) {
				result = middle;
				upper = middle - 1;
			} else {
				lower = middle + 1;
			}
		}
		return result;
	}

	static boolean isPossible(int R, int B, int[] M, int[] S, int[] P, long time) {
		long[] capacities = IntStream.range(0, M.length).mapToLong(i -> Math.min(M[i], Math.max(0, time - P[i]) / S[i]))
				.boxed().sorted(Collections.reverseOrder()).mapToLong(x -> x).toArray();

		long sum = 0;
		for (int i = 0; i < R; i++) {
			sum += capacities[i];

			if (sum >= B) {
				return true;
			}
		}

		return false;
	}
}
