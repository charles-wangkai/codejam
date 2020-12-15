import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int[] R = new int[N];
			int[] C = new int[N];
			for (int i = 0; i < N; i++) {
				R[i] = sc.nextInt();
				C[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(R, C)));
		}

		sc.close();
	}

	static int solve(int[] R, int[] C) {
		int minR = Arrays.stream(R).min().getAsInt();
		int maxR = Arrays.stream(R).max().getAsInt();
		int minC = Arrays.stream(C).min().getAsInt();
		int maxC = Arrays.stream(C).max().getAsInt();

		int result = Integer.MAX_VALUE;
		for (int r = minR; r <= maxR; r++) {
			for (int c = minC; c <= maxC; c++) {
				result = Math.min(result, computeTurnNum(R, C, r, c));
			}
		}
		return result;
	}

	static int computeTurnNum(int[] R, int[] C, int r, int c) {
		return IntStream.range(0, R.length).map(i -> Math.max(Math.abs(R[i] - r), Math.abs(C[i] - c))).max().getAsInt();
	}
}