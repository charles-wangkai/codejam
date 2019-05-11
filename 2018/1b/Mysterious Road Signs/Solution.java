import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int S = sc.nextInt();
			int[] D = new int[S];
			int[] A = new int[S];
			int[] B = new int[S];
			for (int i = 0; i < S; i++) {
				D[i] = sc.nextInt();
				A[i] = sc.nextInt();
				B[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(D, A, B)));
		}

		sc.close();
	}

	static String solve(int[] D, int[] A, int[] B) {
		int S = D.length;

		int[] lefts = new int[S];
		int[] rights = new int[S];
		for (int i = 0; i < S; i++) {
			lefts[i] = D[i] + A[i];
			rights[i] = D[i] - B[i];
		}

		int maxLength = -1;
		int countForMaxLength = -1;
		for (int i = 0; i < S; i++) {
			for (int j = i; j < S; j++) {
				if (isValid(lefts, rights, i, j)) {
					int length = j - i + 1;

					if (length > maxLength) {
						maxLength = length;
						countForMaxLength = 1;
					} else if (length == maxLength) {
						countForMaxLength++;
					}
				}
			}
		}
		return String.format("%d %d", maxLength, countForMaxLength);
	}

	static boolean isValid(int[] lefts, int[] rights, int beginIndex, int endIndex) {
		return IntStream.rangeClosed(beginIndex, endIndex).filter(i -> lefts[i] != lefts[beginIndex])
				.map(i -> rights[i]).distinct().count() <= 1
				|| IntStream.rangeClosed(beginIndex, endIndex).filter(i -> rights[i] != rights[beginIndex])
						.map(i -> lefts[i]).distinct().count() <= 1;
	}
}
