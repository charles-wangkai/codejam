import java.util.Scanner;

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

	static long solve(int[] C, int[] D, int K) {
		int N = C.length;

		int[][] stC = buildSt(C);
		int[][] stD = buildSt(D);

		long result = 0;
		for (int i = 0; i < N; i++) {
			int leftCIndex = findLeftC(C, stC, i);
			int rightCIndex = findRightC(C, stC, i);

			result += computePairNum(leftCIndex, rightCIndex, D, stD, i, C[i] + K)
					- computePairNum(leftCIndex, rightCIndex, D, stD, i, C[i] - K - 1);
		}
		return result;
	}

	static long computePairNum(int leftCIndex, int rightCIndex, int[] D, int[][] stD, int index, int maxValueD) {
		int leftDIndex = findLeftD(D, stD, index, maxValueD);
		int rightDIndex = findRightD(D, stD, index, maxValueD);

		if (leftDIndex == -1 || rightDIndex == -1) {
			return 0;
		}

		int L = Math.max(leftCIndex, leftDIndex);
		int R = Math.min(rightCIndex, rightDIndex);

		return (index - L + 1L) * (R - index + 1);
	}

	static int findLeftC(int[] C, int[][] stC, int index) {
		int result = -1;
		int lower = 0;
		int upper = index;
		while (lower <= upper) {
			int middle = (lower + upper) / 2;

			if (rmq(C, stC, middle, index) == index) {
				result = middle;
				upper = middle - 1;
			} else {
				lower = middle + 1;
			}
		}
		return result;
	}

	static int findRightC(int[] C, int[][] stC, int index) {
		int result = -1;
		int lower = index;
		int upper = C.length - 1;
		while (lower <= upper) {
			int middle = (lower + upper) / 2;

			if (rmq(C, stC, index, middle) == index) {
				result = middle;
				lower = middle + 1;
			} else {
				upper = middle - 1;
			}
		}
		return result;
	}

	static int findLeftD(int[] D, int[][] stD, int index, int maxValueD) {
		int result = -1;
		int lower = 0;
		int upper = index;
		while (lower <= upper) {
			int middle = (lower + upper) / 2;

			if (D[rmq(D, stD, middle, index)] <= maxValueD) {
				result = middle;
				upper = middle - 1;
			} else {
				lower = middle + 1;
			}
		}
		return result;
	}

	static int findRightD(int[] D, int[][] stD, int index, int maxValueD) {
		int result = -1;
		int lower = index;
		int upper = D.length - 1;
		while (lower <= upper) {
			int middle = (lower + upper) / 2;

			if (D[rmq(D, stD, index, middle)] <= maxValueD) {
				result = middle;
				lower = middle + 1;
			} else {
				upper = middle - 1;
			}
		}
		return result;
	}

	static int rmq(int[] values, int[][] st, int beginIndex, int endIndex) {
		int j = log2(endIndex - beginIndex + 1);
		int length = 1 << j;

		if (values[st[beginIndex][j]] >= values[st[endIndex - length + 1][j]]) {
			return st[beginIndex][j];
		} else {
			return st[endIndex - length + 1][j];
		}
	}

	static int[][] buildSt(int[] values) {
		int N = values.length;

		int[][] st = new int[N][];
		for (int i = 0; i < st.length; i++) {
			st[i] = new int[log2(N - i) + 1];

			st[i][0] = i;
		}

		for (int j = 1, length = 2; length <= N; j++, length *= 2) {
			for (int i = 0; i + length <= N; i++) {
				if (values[st[i][j - 1]] >= values[st[i + length / 2][j - 1]]) {
					st[i][j] = st[i][j - 1];
				} else {
					st[i][j] = st[i + length / 2][j - 1];
				}
			}
		}

		return st;
	}

	static int log2(int x) {
		return Integer.toBinaryString(x).length() - 1;
	}
}
