import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			String N = sc.next();

			System.out.println(String.format("Case #%d: %s", tc, solve(N)));
		}

		sc.close();
	}

	static String solve(String N) {
		StringBuilder A = new StringBuilder();
		StringBuilder B = new StringBuilder();
		for (char digit : N.toCharArray()) {
			if (digit == '4') {
				A.append('1');
				B.append('3');
			} else {
				A.append(digit);

				if (B.length() != 0) {
					B.append('0');
				}
			}
		}

		return String.format("%s %s", A, B);
	}
}
