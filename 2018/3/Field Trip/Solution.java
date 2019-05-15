import java.util.Arrays;
import java.util.Scanner;

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

		return Math.max((maxR - minR + 1) / 2, (maxC - minC + 1) / 2);
	}
}
