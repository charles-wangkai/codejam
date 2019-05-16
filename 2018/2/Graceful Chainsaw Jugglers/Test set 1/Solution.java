import java.util.Arrays;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int B = sc.nextInt();

			System.out.println(String.format("Case #%d: %d", tc, solve(R, B)));
		}

		sc.close();
	}

	static int solve(int R, int B) {
		int[][] maxWays = buildEmptyMaxWays(R, B);

		maxWays[R][B] = 0;
		for (int r = 0; r <= R; r++) {
			for (int b = 0; b <= B; b++) {
				if (r == 0 && b == 0) {
					continue;
				}

				int[][] addedMaxWays = buildEmptyMaxWays(R, B);

				for (int i = 0; i <= R; i++) {
					for (int j = 0; j <= B; j++) {
						if (maxWays[i][j] >= 0 && i >= r && j >= b) {
							addedMaxWays[i - r][j - b] = maxWays[i][j] + 1;
						}
					}
				}

				for (int i = 0; i <= R; i++) {
					for (int j = 0; j <= B; j++) {
						maxWays[i][j] = Math.max(maxWays[i][j], addedMaxWays[i][j]);
					}
				}
			}
		}

		int result = -1;
		for (int i = 0; i <= R; i++) {
			for (int j = 0; j <= B; j++) {
				result = Math.max(result, maxWays[i][j]);
			}
		}
		return result;
	}

	static int[][] buildEmptyMaxWays(int R, int B) {
		int[][] result = new int[R + 1][B + 1];
		for (int i = 0; i < result.length; i++) {
			Arrays.fill(result[i], -1);
		}
		return result;
	}
}