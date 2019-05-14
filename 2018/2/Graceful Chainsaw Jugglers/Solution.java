import java.util.Scanner;

public class Solution {
	static int maxChosenNum;

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
		maxChosenNum = -1;

		search(R, B, -1, Integer.MAX_VALUE, 0);

		return maxChosenNum;
	}

	static void search(int remainR, int remainB, int chosenNum, int maxB, int r) {
		maxChosenNum = Math.max(maxChosenNum, chosenNum);

		if (r * (maxChosenNum - chosenNum) > remainR) {
			return;
		}

		int count = 0;

		for (int b = 0; b <= maxB; b++) {
			count++;
			remainR -= r;
			remainB -= b;

			if (remainR < 0 || remainB < 0) {
				break;
			}

			search(remainR, remainB, chosenNum + count, b, r + 1);
		}
	}
}
