import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int[][] A = new int[N][N];
			for (int r = 0; r < N; r++) {
				for (int c = 0; c < N; c++) {
					A[r][c] = sc.nextInt();
				}
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(A)));
		}

		sc.close();
	}

	static int solve(int[][] A) {
		int N = A.length;

		int result = Integer.MAX_VALUE;
		for (int code = (1 << (N * N)) - 1; code >= 0; code--) {
			boolean[][] kepts = decode(N, code);

			if (isValid(A, kepts)) {
				result = Math.min(result, countNotKept(kepts));
			}
		}
		return result;
	}

	static boolean[][] decode(int N, int code) {
		boolean[][] result = new boolean[N][N];
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				result[r][c] = code % 2 == 0;

				code /= 2;
			}
		}
		return result;
	}

	static boolean isValid(int[][] A, boolean[][] kepts) {
		int N = A.length;

		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (kepts[r][c]) {
					for (int i = 0; i < N; i++) {
						if ((i != c && kepts[r][i] && A[r][c] == A[r][i])
								|| (i != r && kepts[i][c] && A[r][c] == A[i][c])) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	static int countNotKept(boolean[][] kepts) {
		int N = kepts.length;

		int result = 0;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (!kepts[r][c]) {
					result++;
				}
			}
		}
		return result;
	}
}