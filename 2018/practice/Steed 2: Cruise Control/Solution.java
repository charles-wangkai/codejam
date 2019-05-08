import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int D = sc.nextInt();
			int N = sc.nextInt();
			int[] K = new int[N];
			int[] S = new int[N];
			for (int i = 0; i < N; i++) {
				K[i] = sc.nextInt();
				S[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %f", tc, solve(D, K, S)));
		}

		sc.close();
	}

	static double solve(int D, int[] K, int[] S) {
		return D / IntStream.range(0, K.length).mapToDouble(i -> (double) (D - K[i]) / S[i]).max().getAsDouble();
	}
}
