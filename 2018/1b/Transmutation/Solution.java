import java.util.Arrays;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int M = sc.nextInt();
			int[][] R = new int[M][2];
			for (int i = 0; i < M; i++) {
				R[i][0] = sc.nextInt() - 1;
				R[i][1] = sc.nextInt() - 1;
			}
			int[] G = new int[M];
			for (int i = 0; i < G.length; i++) {
				G[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(R, G)));
		}

		sc.close();
	}

	static long solve(int[][] R, int[] G) {
		long lower = 0;
		long upper = Arrays.stream(G).asLongStream().sum();
		long result = -1;
		while (lower <= upper) {
			long middle = (lower + upper) / 2;

			if (isPossible(R, G, middle)) {
				result = middle;
				lower = middle + 1;
			} else {
				upper = middle - 1;
			}
		}
		return result;
	}

	static boolean isPossible(int[][] R, int[] G, long targetLead) {
		long[] grams = Arrays.stream(G).asLongStream().toArray();
		grams[0] -= targetLead;

		for (int i = 0; i < R.length; i++) {
			if (Arrays.stream(grams).sum() < 0) {
				return false;
			}

			boolean found = false;

			long[] nextGrams = Arrays.copyOf(grams, grams.length);
			for (int metal = 0; metal < grams.length; metal++) {
				if (grams[metal] < 0) {
					found = true;

					nextGrams[R[metal][0]] += grams[metal];
					nextGrams[R[metal][1]] += grams[metal];
					nextGrams[metal] -= grams[metal];
				}
			}

			if (!found) {
				return true;
			}

			grams = nextGrams;
		}
		return false;
	}
}
